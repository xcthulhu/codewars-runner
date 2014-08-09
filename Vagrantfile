# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "codewars_runner_host"
  config.vm.box = "https://cloud-images.ubuntu.com/vagrant/trusty/current/trusty-server-cloudimg-amd64-vagrant-disk1.box" # 64 bit

  config.vm.provision "shell", path: 'setup/provision.sh'

  config.vm.provision "docker" do |d|
    d.build_image "/vagrant", args: "-t codewars/cli-runner"
    d.build_image "/vagrant/jvm-runner", args: "-t codewars/jvm-runner"
  end

  config.vm.provider "virtualbox" do |v|
    v.memory = 1536
    v.name = "codewars_runner_host"
    config.vm.provision "shell", path: 'setup/dev.sh'
  end

  # Provision Using Puppet
  config.vm.provision :puppet do |puppet|
    puppet.manifests_path = 'puppet/manifests'
    puppet.module_path = ['puppet/modules/']
    puppet.options = ['--verbose']
  end

  # Setting up a static network on 10.100.150.0 class C subnet
  # This is to make future expansion of the vagrant file to a small test cluster easier
  config.vm.network "private_network", ip: "10.100.150.2"
  config.vm.network "forwarded_port", guest: 8080, host: 8080
end
