# Enregistre le systeme de fluides dans uranium.mcreator
# A lancer avec MCreator FERME. Cree une sauvegarde uranium.mcreator.bak_register
$ErrorActionPreference = 'Stop'
$p = Join-Path $PSScriptRoot 'uranium.mcreator'
$t = [IO.File]::ReadAllText($p)

if ($t.Contains('"name":"Fluidpush"')) { Write-Host 'Deja enregistre, rien a faire.'; exit 0 }

Copy-Item $p "$p.bak_register" -Force

$dep = '"dependencies":[{"name":"x","type":"number"},{"name":"y","type":"number"},{"name":"z","type":"number"},{"name":"world","type":"world"}]'
$depB = '"dependencies":[{"name":"x","type":"number"},{"name":"y","type":"number"},{"name":"z","type":"number"},{"name":"world","type":"world"},{"name":"blockstate","type":"blockstate"}]'

function Blk($n)  { '{"name":"' + $n + '","type":"block","compiles":true,"locked_code":false,"registry_name":"' + $n.ToLower() + '","metadata":{"files":[]},"path":"~"},' }
function Proc($n, $d) { '{"name":"' + $n + '","type":"procedure","compiles":true,"locked_code":false,"registry_name":"' + $n.ToLower() + '","metadata":{' + $d + ',"files":[]},"path":"~"},' }

$entries = ''
foreach ($b in 'Fluidpipe','Fluidtank','Fluidpump') { $entries += Blk $b }
foreach ($n in 'Fluidpush','Fluidpumptick','Fluidpipeadded','Fluidtankadded','Fluidpumpadded','Reactorcooling') { $entries += Proc $n $dep }
$entries += Proc 'Centrifugecooling' $depB

$a1 = '"mod_elements": ['
if (-not $t.Contains($a1)) { throw 'Ancre mod_elements introuvable' }
$t = $t.Replace($a1, $a1 + $entries)

$a2 = '"item.uranium.solarpanel": "Solar panel",'
if (-not $t.Contains($a2)) { throw 'Ancre langue introuvable' }
$lang = $a2 + "`n      " + '"item.uranium.fluidpipe": "Fluid pipe",' + "`n      " + '"item.uranium.fluidtank": "Fluid tank",' + "`n      " + '"item.uranium.fluidpump": "Fluid pump",'
$t = $t.Replace($a2, $lang)

[IO.File]::WriteAllText($p, $t, (New-Object System.Text.UTF8Encoding($false)))
Write-Host 'OK : 3 blocs + 7 procedures enregistres. Ouvre MCreator.'
