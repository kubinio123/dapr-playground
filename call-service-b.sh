dapr publish --publish-app-id service-a --pubsub messages-pub-sub --topic messages --data '{"msg": "my"}'
dapr publish --publish-app-id service-a --pubsub messages-pub-sub --topic messages --data '{"msg": "name"}'
dapr publish --publish-app-id service-a --pubsub messages-pub-sub --topic messages --data '{"msg": "is"}'
dapr publish --publish-app-id service-a --pubsub messages-pub-sub --topic messages --data '{"msg": "jacob"}'

dapr invoke --app-id service-b --method get-messages --verb GET