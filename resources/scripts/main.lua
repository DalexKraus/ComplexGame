
entities = {
	player = nil;
}

function init()
	Map.changeMap("maps/simpleMap.tmx", "textures/base.png", 16);
	Map.setScale(3);

  	entities.player = Entity.create(0, 10, LivingType.NEUTRAL, 200, "scripts/entity/player.lua");
end

function draw()
    entities.player:draw();
end

function update()
	-- Update calls going here
end
