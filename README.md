### _Picture gallery for a lifetime of pictures_

Yes, this is yet another gallery application.
I'm just not fully satisfied with existing solutions and like to do some Java development.
Don't try to run this in production, or even better, don't try to use it.
It's just for me and I'm not planning to make it usable for anyone else.
Code is published here because I like showing what I'm working on.

**Run the database**
```bash
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d --net=host postgres
```

**Continuous compilation**
```bash
./gradlew build --continuous
```

**Run it**
```bash
./gradlew bootRun
```