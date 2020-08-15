/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tweet.extractor;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import java.util.stream.Collectors;
//import org.projectspinoza.twitterswissarmyknife.TwitterSwissArmyKnife;
import twitter4j.TwitterStream;
import twitter4j.*;
import twitter4j.conf.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import tweet.extractor.*;


//public interface StatusListener extends StreamListener
public final class PrintSampleStream {
    /**
     * Main entry of this application.
     *
     * @param args arguments doesn't take effect with this example
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public static void main(String[] args) throws TwitterException {

        Twitter twittFactory = getTwitterinstance();
        TwitterStream twittStreamFactory = getTwitterStreamInstance();

        List<String> timeline = getTimeLine();

        for(int i = 0; i < timeline.size(); i++) {
            System.out.println(timeline.get(i));
        }

        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/twitter?useUnicode=yes&characterEncoding=UTF8&" + "user=root&password=root");
            TweetListener2 list = new TweetListener2(conn); //useUnicode=true&amp;characterEncoding=UTF-8
            twittStreamFactory.addListener(list);
            twittStreamFactory.sample();
        }
        catch(SQLException e){
            System.out.println(e);
        }



        //TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        //twitterStream.addListener(listener);

        //twitterStream.sample();



        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }


            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }


            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }


            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };



    }

    public static ConfigurationBuilder getTwitterConnection() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");

        return cb;

    }

    public static TwitterStream getTwitterStreamInstance() {


        ConfigurationBuilder cb = getTwitterConnection();

        TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        TwitterStream twitter = tf.getInstance();

        return twitter;
    }


    public static Twitter getTwitterinstance() {

        ConfigurationBuilder cb = getTwitterConnection();

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        return twitter;
    }


    public static List<String> getTimeLine() throws TwitterException {
        Twitter twitter = getTwitterinstance();

        return twitter.getHomeTimeline().stream()
                .map(item -> item.getText())
                .collect(Collectors.toList());
    }

}