# Wand of Sparking

A **wand of sparking** is a casting weapon that is used to shoot sparking.

| Statistics ||
| - | - |
| Casting Damage | 3 (Magic) |
| Knockback | 0 |
| Mana Consumption| 2 |
| Velocity | 1.2 |
| Rarity | Uncommon |

## Usage

### Casting Attack

Pressing use while holding a wand of sparking in main hand shoots a sparking and consumes 2 mana.

#### Mana Consumption with Mana Efficiency

| Base Mana Consumption | Mana Efficiency I | Mana Efficiency II | Mana Efficiency III | Mana Efficiency IV | Mana Efficiency V |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 2.0 | 1.8 | 1.6 | 1.4 | 1.2 | 1.0 |

#### Sparking

A **sparking** is a projectile that flies along a parabolic curve at the speed of 1.2 blocks per tick and deals 3 magic damage and inflicts fire on collision.

It has a lifespan of 20 ticks. It can pierce once, reducing its remaining lifespan by half.

It has a 5/6 (83.33%) chance to inflict fire for 1–2 seconds, and a 1/6 (16.67%) chance for 2–4 seconds.

## Data Powered

An item where `minecraft:custom_data.id` is "wand_of_sparking:wand_of_sparking" is considered as a wand of sparking.

### Give Command

```mcfunction
/give @s minecraft:stick[custom_data={id:"wand_of_sparking:wand_of_sparking"},enchantable={value:15},item_name={text:"Wand of Sparking"},rarity="uncommon"]
```

### Loot Table Entry

```json
{
    "type": "minecraft:item",
    "functions": [
        {
            "function": "minecraft:set_components",
            "components": {
                "minecraft:custom_data": {
                    "id": "wand_of_sparking:wand_of_sparking"
                },
                "minecraft:enchantable": {
                    "value": 15
                },
                "minecraft:rarity": "uncommon"
            }
        },
        {
            "function": "minecraft:set_name",
            "name": {
                "text": "Wand of Sparking"
            },
            "target": "item_name"
        }
    ],
    "name": "minecraft:stick"
}
```

## Configuration

Below is a template config file `config/wand-of-sparking.json` filled with default values. You may only need to write the lines you would like to modify.

```json
{
    "manaConsumption": 2.0,
    "movementSpeed": 1.2,
    "dissipationFuse": 20,
    "castingDamage": 3.0,
    "pierceCount": 1,
    "isParticleVisible": true
}
```

## Trivia

- Inspired by [Wand of Sparking](https://terraria.wiki.gg/wiki/Wand_of_Sparking) in [Terraria](https://www.terraria.org).
