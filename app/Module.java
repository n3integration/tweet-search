import actors.TwitterSearchActor;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import play.Configuration;
import play.libs.akka.AkkaGuiceSupport;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public void configure() {
        bindActor(TwitterSearchActor.class, "search-actor");
    }

    @Provides
    public Twitter providesTwitter(Configuration cfg) {
        TwitterFactory factory = new TwitterFactory(builder(cfg).build());
        return factory.getInstance();
    }

    private ConfigurationBuilder builder(Configuration cfg) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(cfg.getString("twitter.consumer.key"))
            .setOAuthConsumerSecret(cfg.getString("twitter.consumer.secret"))
            .setOAuthAccessToken(cfg.getString("twitter.access.token"))
            .setOAuthAccessTokenSecret(cfg.getString("twitter.access.secret"));
        return cb;
    }
}
