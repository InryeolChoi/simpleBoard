all: up
up:
	docker compose up -d

down:
	docker compose down

re: down up

logs:
	docker compose logs $(SERVICE)

ps:
	docker compose ps

clean: down
	docker system prune -f
	docker rmi -f $$(docker images -q) || true

fclean: clean
	docker volume rm $$(docker volume ls -q) || true
	docker network rm $$(docker network ls -q) || true

delete:
	docker images prune
	docker volume prune
	docker network prune
	docker buildx prune

.PHONY: all up down re logs ps clean
