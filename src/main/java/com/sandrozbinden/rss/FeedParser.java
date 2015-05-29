package com.sandrozbinden.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sandrozbinden.rss.exception.InvalidFeedInput;

public class FeedParser {

    public Feed parse(String unparsed) {
        String[] split = unparsed.split("\\|");
        if (split.length != 9) {
            throw new InvalidFeedInput("The current feed: " + unparsed + " does not contain 8 pipe (|) symbols");
        }
        Feed feed = new Feed();
        feed.setId(split[0]);
        feed.setNewspaper(split[1]);
        feed.setLanguage(split[2]);
        feed.setTitle(split[3]);
        feed.setLink(split[4]);
        feed.setPublishingDate(parseDate(split[5]));
        feed.setText(split[6]);
        return feed;
    }

    private Date parseDate(String unparsedDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(unparsedDate);
        } catch (ParseException e) {
            throw new RuntimeException("Wrong format: " + unparsedDate, e);
        }
    }

}
