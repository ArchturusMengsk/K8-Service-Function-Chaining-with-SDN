#credit for threading portion to: https://realpython.com/intro-to-python-threading/
import logging
import threading
import time

def thread_function1(name):
    logging.info("Thread %s: starting", name)
    import forward_from_eth1_to_eth2
    logging.info("Thread %s: finishing", name)

def thread_function2(name):
    logging.info("Thread %s: starting", name)
    import forward_from_eth2_to_eth1
    logging.info("Thread %s: finishing", name)

if __name__ == "__main__":
    format = "%(asctime)s: %(message)s"
    logging.basicConfig(format=format, level=logging.INFO,
                        datefmt="%H:%M:%S")

    logging.info("Main    : before creating thread")
    x = threading.Thread(target=thread_function1, args=("eth1_to_eth2",))
    y = threading.Thread(target=thread_function2, args=("eth2_to_eth1",))
    logging.info("Main    : before running thread")
    x.start()
    y.start()
    logging.info("Main    : wait for the thread to finish")
    # x.join()
    # y.join()
    logging.info("Main    : all done")