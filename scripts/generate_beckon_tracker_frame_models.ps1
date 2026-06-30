$root = Split-Path -Parent $PSScriptRoot
$baseDir = Join-Path $root 'src/main/resources/assets/wherearayoudamnit/models/item'

$sets = @(
    @{ Name = 'searching'; Count = 24 },
    @{ Name = 'found'; Count = 31 }
)

foreach ($set in $sets) {
    $outDir = Join-Path $baseDir $set.Name
    New-Item -ItemType Directory -Path $outDir -Force | Out-Null

    for ($i = 0; $i -lt $set.Count; $i++) {
        $content = @"
{
    "parent": "item/handheld",
    "textures": {
        "layer0": "wherearayoudamnit:$($set.Name)/beckon_tracker_$($set.Name)_$i"
    },
    "display": {
        "thirdperson_righthand": {
            "rotation": [0, 0, 0],
            "translation": [0, 3, 1],
            "scale": [0.55, 0.55, 0.55]
        },
        "firstperson_righthand": {
            "rotation": [0, -90, 25],
            "translation": [1.13, 3.2, 1.13],
            "scale": [0.68, 0.68, 0.68]
        },
        "gui": {
            "rotation": [0, 0, 0],
            "translation": [0, 0, 0],
            "scale": [1, 1, 1]
        }
    }
}
"@

        $filePath = Join-Path $outDir "beckon_tracker_$($set.Name)_$i.json"
        Set-Content -Path $filePath -Value $content -Encoding utf8
        Write-Host "Created $filePath"
    }
}
