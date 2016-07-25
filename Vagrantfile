# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "ubuntu/wily64"   # this doesn't work
  #config.vm.box = "ubuntu/vivid64" # this does

  config.vm.network "forwarded_port", guest: 6006, host: 6006 # java debug port

  config.vm.provision "ansible" do |ansible|
    ansible.sudo = true
    ansible.playbook = "ansible/playbook.yml"
  end
end
