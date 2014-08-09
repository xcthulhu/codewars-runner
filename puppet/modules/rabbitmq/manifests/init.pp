class rabbitmq {
	require docker

	file {'/var/lib/rabbitmq':
		ensure => directory,
	}
        ->
	file {'/var/lib/rabbitmq/mnesia':
		ensure => directory,
	}
        ->
	docker::image { 'mikaelhg/docker-rabbitmq': }
        ->
	docker::run { 'rabbitmq':
		image   => 'dockerfile/docker-rabbitmq',
		ports   => ['5672:5672', '15672:15672'],
		volumes => ['/var/lib/rabbitmq/mnesia:/var/lib/rabbitmq/mnesia'],
	}
}
