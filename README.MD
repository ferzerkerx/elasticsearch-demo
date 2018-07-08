# Elastic Search Demo

[![Build Status](https://travis-ci.org/ferzerkerx/elasticsearch-demo.svg?branch=master)](https://travis-ci.org/ferzerkerx/elasticsearch-demo)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.ferzerkerx.elasticsearch%3Aelasticsearch-demo&metric=alert_status)](https://sonarcloud.io/dashboard/index/com.ferzerkerx.elasticsearch%3Aelasticsearch-demo)


SpringBoot application that pushes to ES and then reads from it and prints it for learning purposes

Dependencies:
Assumes Elastic Search is running on localhost:9200

I recommend running ELK using this docker image:
https://github.com/spujadas/elk-docker

Then I just followed instructions:
- sudo docker run -p 5601:5601 -p 9200:9200 -p 5044:5044 -it --name elk sebp/elk
- If this error shows up : 
max virtual memory areas vm.max_map_count [65530] is too low, increase to at least can be fixed by this https://github.com/spujadas/elk-docker/issues/92