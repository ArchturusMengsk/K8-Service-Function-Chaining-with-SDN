./DockerCleanup.sh
./removeimages.sh
cd ..
cd Forwarder/Ubuntu
sudo docker image build -t forwarder:v0.3 .
cd ..
cd ..
cd Sender/Alpine
sudo docker image build -t sender:v0.1 .