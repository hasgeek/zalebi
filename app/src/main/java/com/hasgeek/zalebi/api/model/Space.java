package com.hasgeek.zalebi.api.model;

import org.parceler.Parcel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by karthik on 23-12-2014.
 */
@Parcel
public class Space {

        @SerializedName("bg_color")
        @Expose
        private String bgColor;
        @SerializedName("bg_image")
        @Expose
        private String bgImage;
        @Expose
        private String datelocation;
        @Expose
        private String end;
        @SerializedName("explore_url")
        @Expose
        private String exploreUrl;
        @SerializedName("json_url")
        @Expose
        private String jsonUrl;
        @Expose
        private String name;
        @Expose
        private String start;
        @Expose
        private Integer status;
        @Expose
        private String timezone;
        @Expose
        private String title;
        @Expose
        private String url;
        @Expose
        private String website;

        /**
         *
         * @return
         * The bgColor
         */
        public String getBgColor() {
            return bgColor;
        }

        /**
         *
         * @param bgColor
         * The bg_color
         */
        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        /**
         *
         * @return
         * The bgImage
         */
        public String getBgImage() {
            return bgImage;
        }

        /**
         *
         * @param bgImage
         * The bg_image
         */
        public void setBgImage(String bgImage) {
            this.bgImage = bgImage;
        }

        /**
         *
         * @return
         * The datelocation
         */
        public String getDatelocation() {
            return datelocation;
        }

        /**
         *
         * @param datelocation
         * The datelocation
         */
        public void setDatelocation(String datelocation) {
            this.datelocation = datelocation;
        }

        /**
         *
         * @return
         * The end
         */
        public String getEnd() {
            return end;
        }

        /**
         *
         * @param end
         * The end
         */
        public void setEnd(String end) {
            this.end = end;
        }

        /**
         *
         * @return
         * The exploreUrl
         */
        public String getExploreUrl() {
            return exploreUrl;
        }

        /**
         *
         * @param exploreUrl
         * The explore_url
         */
        public void setExploreUrl(String exploreUrl) {
            this.exploreUrl = exploreUrl;
        }

        /**
         *
         * @return
         * The jsonUrl
         */
        public String getJsonUrl() {
            return jsonUrl;
        }

        /**
         *
         * @param jsonUrl
         * The json_url
         */
        public void setJsonUrl(String jsonUrl) {
            this.jsonUrl = jsonUrl;
        }

        /**
         *
         * @return
         * The name
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @param name
         * The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         *
         * @return
         * The start
         */
        public String getStart() {
            return start;
        }

        /**
         *
         * @param start
         * The start
         */
        public void setStart(String start) {
            this.start = start;
        }

        /**
         *
         * @return
         * The status
         */
        public Integer getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(Integer status) {
            this.status = status;
        }

        /**
         *
         * @return
         * The timezone
         */
        public String getTimezone() {
            return timezone;
        }

        /**
         *
         * @param timezone
         * The timezone
         */
        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        /**
         *
         * @return
         * The title
         */
        public String getTitle() {
            return title;
        }

        /**
         *
         * @param title
         * The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         *
         * @return
         * The url
         */
        public String getUrl() {
            return url;
        }

        /**
         *
         * @param url
         * The url
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         *
         * @return
         * The website
         */
        public String getWebsite() {
            return website;
        }

        /**
         *
         * @param website
         * The website
         */
        public void setWebsite(String website) {
            this.website = website;
        }

}
