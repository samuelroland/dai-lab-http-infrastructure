# Froom - DAI Lab - HTTP infrastructure

Froom is just another forum system with strange features. Here is how we developed and deployed the Froom infrastructure.

## Static website
We placed all files for the static website under [froom-static](froom-static/).

Here are the files we created:
- `index.html`: We have created our own simple `index.html` with a little landing page for Froom.
- `Dockerfile`
  ```dockerfile
	# based on Nginx image from Docker Hub
	FROM nginx 
	# Copy the nginx configuration at one of the default configuration place
	COPY nginx.conf /etc/nginx/nginx.conf 
	# copy our website in the folder dedicated to this website (defined in nginx.conf)
	COPY index.html /var/www/froom/index.html 
  ```
- `nginx.conf`
	```nginx 
	# Necessary to avoid error 'nginx: [emerg] no "events" section in configuration'
	events{}	

	http {
		server {
			# Listening port, the standard HTTP port
			listen       80;
			# The domain name of this website, useful if we want several website on the server IP
			server_name  froom.local;
			# The root folder of our website (we copied our index.html there)
			root         /var/www/froom;
		}
	}
	```

To build the image (tagged as `froom` to easily identify it)
```
sudo docker build -t froom .
```
And to start a container from this image
```
sudo docker run -p 8080:80 froom
```

You can now access on your host machine the website under the port `8080`. First configure `127.0.0.1	 froom.local` in your hosts file then you can access the static website on `froom.local:8080`.

![froom-static-up.png](imgs/froom-static-up.png)

## Step 2 - Docker compose 

```yml
services:
# a new service for the static website
  froom-static:
    # to refer the froom-static folder with the Dockerfile inside
    build: 
      context: froom-static
    # Mapped the host's port (80) with the docker container port (80) used by nginx
    ports:
    - "80:80"
    
```

Test for the docker build

```
sudo docker compose build
```
```
[+] Building 0.9s (8/8) FINISHED                                                                                                                                                  docker:default
 => [froom-static internal] load .dockerignore                                                                                                                                              0.0s
 => => transferring context: 2B                                                                                                                                                             0.0s
 => [froom-static internal] load build definition from Dockerfile                                                                                                                           0.0s
 => => transferring dockerfile: 117B                                                                                                                                                        0.0s
 => [froom-static internal] load metadata for docker.io/library/nginx:latest                                                                                                                0.7s
 => [froom-static 1/3] FROM docker.io/library/nginx@sha256:2bdc49f2f8ae8d8dc50ed00f2ee56d00385c6f8bc8a8b320d0a294d9e3b49026                                                                 0.1s
 => => resolve docker.io/library/nginx@sha256:2bdc49f2f8ae8d8dc50ed00f2ee56d00385c6f8bc8a8b320d0a294d9e3b49026                                                                              0.1s
 => [froom-static internal] load build context                                                                                                                                              0.0s
 => => transferring context: 61B                                                                                                                                                            0.0s
 => CACHED [froom-static 2/3] COPY nginx.conf /etc/nginx/nginx.conf                                                                                                                         0.0s
 => CACHED [froom-static 3/3] COPY index.html /var/www/froom                                                                                                                                0.0s
 => [froom-static] exporting to image                                                                                                                                                       0.0s
 => => exporting layers                                                                                                                                                                     0.0s
 => => writing image sha256:40858cee24286c1609221c9426dc64fc03ebf11795f20f52708008818c98f125                                                                                                0.0s
 => => naming to docker.io/library/dai-lab-http-infrastructure-froom-static 
```

## Javalin API

```sh
mvn package
```

```sh
java -jar target/server-1.0-SNAPSHOT.jar
```

## Step 3 - HTTP API server

We have a GET route on / (the inital arriving page) to send a Hello message when arriving on this home page.
```
	app.get("/", ctx -> ctx.result(HELLO_MESSAGE));

```
and then the routes for all the comments with the CRUD operations : Create, Read, Update, Delete.

```
	app.get("/comments/{id}", commentsController::getOne);
	app.get("/comments", commentsController::getAll);
	app.post("/comments", commentsController::create);
	app.delete("/comments/{id}", commentsController::delete);
	app.put("/comments/{id}", commentsController::update);
	
```

We also tested our implementation with Bruno although we have all the tests necessary coded directecly into the `CommentsTests.java` file.

So the test we made is a POST request which creates a new comment :
![bruno test image 1](/imgs/bruno1.png)

and then the content itself with the answer from the server :
![bruno test image 2](/imgs/bruno2.png)

## Step 4 - Reverse proxy
```
sudo docker compose up
```

```
[+] Running 3/0
 ✔ Container infra-labo5-traefik-1       Created                                                                                                                                   0.0s 
 ✔ Container infra-labo5-froom-api-1     Created                                                                                                                                   0.0s 
 ✔ Container infra-labo5-froom-static-1  Created                                                                                                                                   0.0s 
Attaching to infra-labo5-froom-api-1, infra-labo5-froom-static-1, infra-labo5-traefik-1
```

When we test to visit the 2 websites on `localhost:8000` and then `localhost:8000/api/comments`, we see that routing the expected pages and the live logs show Traefik is doing the routing to both containers.

```
infra-labo5-froom-static-1  | 172.19.0.3 - - [11/Jan/2024:08:37:12 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (X11; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
infra-labo5-froom-static-1  | 172.19.0.3 - - [11/Jan/2024:08:37:42 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (X11; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"

infra-labo5-froom-api-1     | New request GET on /api/comments
```

## Step 6 - sticky sessions

To be able to handle a load balacing with sticky session server, we modified the `docker-compose.yml` as so :

First to activate sticky sessions we included into the docker compose file :

```dockerfile
- "traefik.http.services.froom-api.loadbalancer.sticky=true"
```

then we give this cookie a name with the following command :

```dockerfile
- "traefik.http.services.froom-api.loadbalancer.sticky.cookie.name=StickyCookie"
```

and we make it secure by adding this command :

```dockerfile
- "traefik.http.services.froom-api.loadbalancer.sticky.cookie.secure=true"
```

### Demonstration

We did 9 page refreshes on the API to prove that sticky session is working.
Then we did 5 page refreshes on the static website to show that Round-Robin is still active and is working.

```
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-api-1     | New request GET on /api/comments
dai-lab-http-infrastructure-froom-static-3  | 172.22.0.2 - - [12/Jan/2024:16:43:14 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
dai-lab-http-infrastructure-froom-static-4  | 172.22.0.2 - - [12/Jan/2024:16:43:15 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
dai-lab-http-infrastructure-froom-static-1  | 172.22.0.2 - - [12/Jan/2024:16:43:15 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
dai-lab-http-infrastructure-froom-static-5  | 172.22.0.2 - - [12/Jan/2024:16:43:15 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
dai-lab-http-infrastructure-froom-static-2  | 172.22.0.2 - - [12/Jan/2024:16:43:15 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
```