package com.hasgeek.zalebi.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.api.model.Attendee;
import com.hasgeek.zalebi.api.model.ExchangeContact;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.api.model.SyncQueueContact;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncContactsEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSyncAttendeesEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSyncContactsEvent;
import com.hasgeek.zalebi.eventbus.event.contactexchange.ContactScannedEvent;
import com.hasgeek.zalebi.eventbus.event.loader.LoadSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SpacesLoadedEvent;
import com.orm.SugarTransactionHelper;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.FormattedName;
import ezvcard.property.Kind;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
public class ContactExchangeService {

    private String LOG_TAG = "ContactExchangeService";
    private Bus mBus;
    Context ctx;
    SharedPreferences prefs;

    public static boolean syncStatus;

    public ContactExchangeService(Bus bus, Context ctx) {
        mBus = bus;
        this.ctx = ctx;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Subscribe
    public void onContactSyncedEvent(APIResponseSyncContactsEvent event) {
        Log.i(LOG_TAG, "onContactSyncedEvent() SUBSCRIPTION APIResponseSyncContactsEvent");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            JSONObject obj = new JSONObject(event.getResponse());
            ExchangeContact contact = gson.fromJson(obj.optString("participant","{}"), ExchangeContact.class);
            List<ExchangeContact> foundContacts = ExchangeContact.find(ExchangeContact.class,"user_id = ?", contact.getUserId());
            if(foundContacts.isEmpty()){
                contact.save();
            }
            else {
                for(ExchangeContact c: foundContacts) {
                    c.delete();
                }
                contact.save();
                //TODO: Contact already exists - just double checking
            }

            List<SyncQueueContact> syncQueueContacts = SyncQueueContact.find(SyncQueueContact.class,"user_id = ?", contact.getUserId());
            for(SyncQueueContact syncQueueContact: syncQueueContacts) {
                syncQueueContact.delete();
            }


        } catch (Exception e) {
            e.printStackTrace();
            mBus.post(new APIErrorEvent(e.getMessage()));
        }

    }

    @Subscribe
    public void onAttendeeSyncedEvent(APIResponseSyncAttendeesEvent event) {
        Log.i(LOG_TAG, "onAttendeeSyncedEvent() SUBSCRIPTION APIResponseSyncAttendeesEvent");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            JSONObject obj = new JSONObject(event.getResponse());
            final List<Attendee> attendees = Arrays.asList(gson.fromJson(obj.optString("participants", "{}"), Attendee[].class));
            Log.d("Attendees", attendees.toString()+"");
            SugarTransactionHelper.doInTansaction(new SugarTransactionHelper.Callback() {
                @Override
                public void manipulateInTransaction() {
                    for(Attendee d: attendees ) {
                        List<Attendee> foundAttendees = Attendee.find(Attendee.class,"user_id = ?", d.getUserId());
                        if(foundAttendees.isEmpty()) {
                            d.save();
                        }
                        else {
                            for(Attendee f: foundAttendees) {
                                f.update(d);
                                f.save();
                            }
                        }
                    }
                }
            });
            Toast.makeText(ctx, "Refreshed event data", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            mBus.post(new APIErrorEvent(e.getMessage()));
        }
    }

    public static Attendee getAttendeeFromScannedData(String data, String spaceId) {
        Attendee result = null;
        if(data!=null) {
            if(data.length()==16) {
                String puk = data.substring(0, 8);
                String key = data.substring(8);
                if(!Attendee.find(Attendee.class, "puk = ?", puk).isEmpty()) {
                    if(ExchangeContact.find(ExchangeContact.class,"puk = ?", puk).isEmpty()) {
                        result = Attendee.find(Attendee.class, "puk = ?", puk).get(0);
                        result.setKey(key);
                    }
                    else {
                        // TODO: Already in contact list
                    }
                }
                else {
                    // TODO: Cannot find Attendee
                }
            }
            else {
                // TODO: invalid data
            }
        }
        return result;
    }

    public static void addAttendeeToSyncQueue(Attendee d) {
        if(SyncQueueContact.find(SyncQueueContact.class,"user_puk = ?", d.getPuk()).isEmpty()) {
            new SyncQueueContact(d.getPuk(), d.getKey(), "metafresh").save();
            BusProvider.getInstance().post(new APIRequestSyncContactsEvent("metarefresh"));
        }
    }

    public static void deleteExchangeContact(ExchangeContact e) {
        e.delete();
    }

    public static List<Attendee> getAttendeeList() {
        List<Attendee> attendees = new ArrayList<>();
        attendees.addAll(Attendee.listAll(Attendee.class));
        return attendees;
    }

    public static List<ExchangeContact> getExchangeContacts() {
        List<ExchangeContact> contacts = new ArrayList<>();
        contacts.addAll(ExchangeContact.listAll(ExchangeContact.class));
        return contacts;
    }

    public static Collection<VCard> getVCardsFromExchangeContacts() {
        Collection<VCard> vCardCollection = new ArrayList<>();
        List<ExchangeContact> exchangeContacts = ExchangeContact.listAll(ExchangeContact.class);
        for(ExchangeContact c: exchangeContacts) {
            VCard vcard = new VCard();

            FormattedName name = new FormattedName(c.getFullname());
            vcard.addFormattedName(name);

            Telephone number = new Telephone(c.getPhone());
            number.addType(TelephoneType.CELL);
            vcard.addTelephoneNumber(number);

            vcard.setOrganization(c.getCompany());
            vcard.addRole(c.getJobTitle());

            Kind kind = Kind.individual();
            vcard.setKind(kind);

            vcard.addNote("Twitter:"+c.getTwitter());

            vcard.setProductId("Talkfunnel by HasGeek");
            vCardCollection.add(vcard);

        }
        return vCardCollection;
    }


}
