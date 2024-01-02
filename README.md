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