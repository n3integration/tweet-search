## Tweet Search App

A basic application that demonstrates a few core concepts of the [Play](http://www.playframework.com) framework including:

* [Actions](https://www.playframework.com/documentation/2.5.x/JavaActionsComposition)
* [Actors](https://www.playframework.com/documentation/2.5.x/JavaAkka)
* [Modules](https://www.playframework.com/documentation/2.5.x/JavaPlayModules)
* [Routes](https://www.playframework.com/documentation/2.5.x/JavaRouting)
* [Streams](http://www.reactive-streams.org/)
* [Templates](https://www.playframework.com/documentation/2.5.x/JavaTemplateUseCases)

### Run the Application
#### Prerequisites: 
* [Typesafe Activator](https://www.lightbend.com/activator/download)

```bash
activator ~run
```

### Build a Docker Image
#### Prerequisites: 
* [Typesafe Activator](https://www.lightbend.com/activator/download)
* [Docker Toolbox](https://www.docker.com/products/docker-toolbox)

```bash
activator docker:publishLocal
```
