# Froom - DAI Lab - HTTP infrastructure

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