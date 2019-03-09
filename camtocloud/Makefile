
all: bark_supervise camtocloud_supervise

bark.o: bark.s
	as -o bark.o bark.s

camtocloud.o: camtocloud.s
	as -o camtocloud.o camtocloud.s

bark_supervise: bark.s
	gcc -o bark_supervise bark.s

camtocloud_supervise: camtocloud.s
	gcc -o camtocloud_supervise camtocloud.s

clean:
	rm -f bark_supervise bark.o
	rm -f camtocloud_supervise camtocloud.o
