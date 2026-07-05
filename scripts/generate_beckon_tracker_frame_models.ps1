$root = Split-Path -Parent $PSScriptRoot
$modelsBaseDir = Join-Path $root 'src/main/resources/assets/wherearayoudamnit/models/item'

$sets = @(
    @{ Name = 'searching'; Count = 26; TextureFolder = 'rooter_tracker_searching'; ModelFolder = 'rooter_tracker_searchingfile' },
    @{ Name = 'found'; Count = 56; TextureFolder = 'rooter_tracker_found'; ModelFolder = 'rooter_tracker_foundfile' }
)

foreach ($set in $sets) {
    $outDir = Join-Path $modelsBaseDir $set.Name
    $outDir = Join-Path $outDir $set.ModelFolder
    New-Item -ItemType Directory -Path $outDir -Force | Out-Null

    for ($i = 0; $i -lt $set.Count; $i++) {
        $texturePath = "wherearayoudamnit:$($set.Name)/$($set.TextureFolder)/$($set.TextureFolder)_$i"
        $content = @"
{
    "parent": "item/handheld",
    "textures": {
        "layer0": "$texturePath"
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

        $filePath = Join-Path $outDir "$($set.ModelFolder)_$i.json"
        Set-Content -Path $filePath -Value $content -Encoding utf8
        Write-Host "Created $filePath"
    }
}
