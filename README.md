# K8-Service-Function-Chaining-with-SDN
In this project, service function chaining is implemented on K8 with an SDN tool (ONOS), or a native K8 plugin (OVN4NFV)

----------------------------------------
Contents
----------------------------------------

* **Forwarder** contains docker files for building packet forwarder image:
  * Ubuntu(Implemented)/Alpine(Not Yet)
* **Sender** contains docker files for building packet sender image:
  * Ubuntu(Implemented)/Alpine(Implemented)
* **Utilities** contains bash scripts for implementing scenarios:
  * Docker Cleanup: Cleans excess of images
  * dumpflowsbr0.sh/dumpflowsbr1.sh: constantly monitors flow dumbs from ovs-br0 and 1 bridges
  * scenarioB/Cpings.sh: Does the testing pings for the appropriate scenarios
  * scenarios.sh: Sets up as well as cleans up all the scenarios

----------------------------------------
Linux Environment Dependencies
----------------------------------------
* curl
* ca-certificates
* gnupg
* lsb-release
* **Docker**
  * echo \ "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \ $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  * sudo apt-get update
  * sudo apt-get install docker-ce docker-ce-cli containerd.io
* git
* git-review
* zip
* unzip
* python && python3
* **python3-pip && python-pip**
  * do the following command after install: pip3 install --upgrade pip
* python3-dev && python-dev
* python3-setuptools && python-setuptools
* The following python packages should be installed with pip3:
  * pip3 install selenium
  * **only within forwarder container**: pip3 install scapy
* **Bazel** (This Bazel install is outdated)
  * wget https://github.com/bazelbuild/bazelisk/releases/download/v1.4.0/bazelisk-linux-amd64
  * chmod +x bazelisk-linux-amd64
  * sudo mv bazelisk-linux-amd64 /usr/local/bin/bazel
* **ONOS**
  * git clone https://gerrit.onosproject.org/onos

----------------------------------------
TO DO
----------------------------------------

-Add kubernetes deployment scripts
-Add Bazel install / compilation guide
