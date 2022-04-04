from scapy.all import *
from scapy.layers.inet import IP

print("Hi")

def change_send(pckt):
    sendp(pckt, iface="eth2")
    print("sending")

while 1:
    sniff(iface="eth1", filter="src host 10.10.10.2", prn=change_send)