sudo docker container exec myContainer1 ping -c 3 10.10.10.2
sudo docker container exec myContainer1 ping -c 3 10.10.10.4
sudo docker container exec myContainer2 ping -c 3 10.10.10.2
sudo docker container exec myContainer2 ping -c 3 10.10.10.4