sudo docker container exec myContainer1 ping -c 3 10.10.10.2
sudo docker container exec myContainer1 ping -c 3 10.10.10.4
sudo docker container exec myContainer1 ping -c 3 10.10.20.5
sudo docker container exec myContainer1 ping -c 3 10.10.20.3
sudo docker container exec myContainer2 ping -c 3 10.10.10.2
sudo docker container exec myContainer2 ping -c 3 10.10.10.4
sudo docker container exec myContainer2 ping -c 3 10.10.20.5
sudo docker container exec myContainer2 ping -c 3 10.10.20.3
sudo docker container exec myContainer3 ping -c 3 10.10.10.2
sudo docker container exec myContainer3 ping -c 3 10.10.10.4
sudo docker container exec myContainer3 ping -c 3 10.10.20.5
sudo docker container exec myContainer3 ping -c 3 10.10.20.3