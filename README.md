## Tweet Search App

A basic application that demonstrates a few core concepts of the [Play](http://www.playframework.com) framework including:

* [Actions](https://www.playframework.com/documentation/2.5.x/JavaActionsComposition)
* [Actors](https://www.playframework.com/documentation/2.5.x/JavaAkka)
* [Modules](https://www.playframework.com/documentation/2.5.x/JavaPlayModules)
* [Routes](https://www.playframework.com/documentation/2.5.x/JavaRouting)
* [Streams](http://www.reactive-streams.org/)
* [Templates](https://www.playframework.com/documentation/2.5.x/JavaTemplateUseCases)

#### Prerequisites: 
* [Typesafe Activator](https://www.lightbend.com/activator/download)
* [Twitter Application](https://apps.twitter.com/)

### Run the Application

```bash
export TWITTER_CONSUMER_KEY=<Consumer Key (API Key)>
export TWITTER_CONSUMER_SECRET=<Consumer Secret (API Secret)>
export TWITTER_ACCESS_TOKEN=<Access Token>
export TWITTER_ACCESS_SECRET=<Access Token Secret>
activator ~run
```

### Build a Zip Distribution

```bash
activator dist
```

### Build a Docker Image
#### Prerequisites: 
* [Docker Toolbox](https://www.docker.com/products/docker-toolbox)

```bash
activator docker:publishLocal
```
