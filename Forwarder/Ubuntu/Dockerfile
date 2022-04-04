FROM ubuntu
RUN apt-get update && apt-get -y install python3.8 && apt-get -y install tcpdump && apt-get -y install net-tools && apt-get -y install iputils-ping && apt-get -y install pip && pip3 install scapy
COPY . /app
WORKDIR /app