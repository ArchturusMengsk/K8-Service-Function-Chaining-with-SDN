from scapy.all import *
from scapy.layers.inet import IP

print("Hello")

def change_send(pckt):
    sendp(pckt, iface="eth1")
    print("sending")

while 1:
    sniff(iface="eth2", filter="src host 10.10.20.3", prn=change_send)