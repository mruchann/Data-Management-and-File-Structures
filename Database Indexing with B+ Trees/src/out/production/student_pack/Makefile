J = java
JC = javac

compile:
	$(JC) *.java

sample_run:
	$(J) CengBookRunner 2 sample_input.txt True

shuffle_run:
	$(J) CengBookRunner 2 shuf_input.txt True

clean:
	$(RM) *.class

all:
	make compile
	make sample_run
	make clean


