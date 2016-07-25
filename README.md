# hbc_status
Small test app to narrow down an issue with hosebird

To fire up vm clone the repository and build the fat jar. This needs to be put into ansible/roles/java/files

Afterwards run vagrant up to provision machine and vagrant ssh to connect.

Run: /opt/run.sh

Vagrantfile exposes port 6006 for java debugging, you can attach to the running instance here.


Vagrantfile also contains a commented line with the working older base box, just swap the two config.vm.box lines and rerun process above.