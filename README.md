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
