# Build Battle
Minijuego basado en 20 jugadores (con un mínimo de 10 jugadores) donde dependiendo de la palabra que se escoja en una votación entre los participantes del minijuego, se tendrá que construir, el tiempo empleado para la construcción debe de ser de 5 minutos. Una vez pasados los 5 minutos todos los participantes irán teletransportándose de una construir a otra ( De los demás participantes ) y votando por cada una de las construcciones.

Este minijuego también debe tener compatibilidad con duos. Donde el tiempo de juego será de 4 minutos.

El tamaño de las parcelas debe de ser de 27 para SOLO y 41 para DUOS

En el hotbar deben de recibir un item de estrella del nether donde podrán cambiar el clima, el tiempo el bioma además de eso tiene que haber una posibilidad de añadir partículas, conseguir bloques adicionales usando heads y estandartes de diferentes tipos.

Se debe de poder cambiar el suelo y poder elegir la altura del suelo entre 1 bloque y 10 bloques de altura (Para las construcciones con agua)

En el momento de la votación si el jugador o equipo recibe una votación favorable de (Legendario) deben de llover diamantes por un momento para que se entienda que fue un éxito su construcción en el momento.

Tiene que haber un total de 9 posibles votos

Super caca - Caca - Mini caca - Meh - Okey - Bueno - Muy bueno - HYPE - Legendario

Cada uno de los posibles votos darán entre 10 a 90 puntos teniendo como Super caca 10 puntos y Legendario 90

En el es.noobcraft.buildbattle.api.scoreboard de la derecha debe de mostrarse en el momento de finalización de las votaciones el top 5 de jugadores con mas puntuación y además de mostrar el top 3 en el chat

En el es.noobcraft.buildbattle.api.scoreboard antes de las votaciones en todo momento tiene que salir el tiempo restante, la construcción que se tiene que hacer, el nombre de tu compañero en el caso de DUOS, el rango dentro del minijuego ( Los rangos irán dependiendo de cuantas partidas has ganado en total y perdido es un RATIO de VICTORIA/PERDIDA

###config.yml
````yaml
settings:
  #Lobby server name
  lobby: build-battle-lobby-01
#Materials of the vote inventory
vote:
  material: STAINED_CLAY
  type:
    - 14
    - 12
    - 7
    - 4
    - 5
    - 13
    - 9
    - 3
    - 11
griffin:
  - BARRIER
#All the available themes on the game
themes:
  - 'SNOWMAN'
  - 'FARM'
  - 'PIG'
  - 'RIVER'
  - 'COMPUTER'
  - 'HOUSE'
  - 'HORSE'
  - 'EGG'
#All available ranks on buildbattle
ranks:
  - 'Default,0'
  - '&8Principiante,2250'
  - '&6Aprendiz,4750'
  - '&fPaisajista,7250'
  - '&bDecorador,9750'
  - '&aArquitecto,12250'
  - '&5Maestro,14750'
  - '&4Experto,17250'
  - '&e&lConstructor,20000'
````

###arenas.yml
```yaml
arenas:
  #Name of the arena
  name: test
  #Server in where the arena is running   
  server: bb_01
  #Is arena enabled
  enabled: true
  #Location of the arena waiting form players
  spawn:
    world: test
    x: 0
    y: 21
    z: 0
    #Location of the corner of the arena
  corner:
    world: world
    x: 393.58
    y: 90.0
    z: 131.77
  settings:
    #Max players on this game
    max_players: 1
    #Time to do countdown
    countdown: 10
    #Time to vote the theme
    vote_theme: 10
    #Time that the game will last
    game_duration: 100
    #Time to vote each construction
    vote_construction: 20
    #Time to stop the game from all regions are voted
    stop_time: 8
    plot:
      #Size of each region
      size: 27
      #Size of the border of the arena
      border: 10
```

###skulls.yml
````yaml
skulls:
  #Identifier of the skukll
  guardian:
    #Display name ofthe skull
    name: Guardian Skull
    #Texture of the skull
    texture: eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZjM0YTcxZTc3MTViNmJhNTJkNWRkMWJhZTVjYjg1Zjc3M2RjOWIwZDQ1N2I0YmZjNWY5ZGQzY2M3Yzk0In19fQ==
````