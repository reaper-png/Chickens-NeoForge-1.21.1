# Third-party license and asset provenance review

Reviewed on 2026-11-21 for the NeoForge 1.21.1 release-candidate source tree.

## Original Chickens
- Source: https://github.com/CreeperHost/Chickens
- License: https://github.com/CreeperHost/Chickens/blob/master/LICENSE.txt
- Preserved notice: `LICENSE-original-MIT.txt` (copyright 2016 setycz)
- The NeoForge port contains 148 byte-identical Git blobs (SHA matches), including 147 resource files and the preserved MIT license. This is evidence of reuse, not an exhaustive historical provenance audit.

## Roost
- Source: https://github.com/timrwood/roost
- License: https://github.com/timrwood/roost/blob/master/license.txt
- Preserved notice: `LICENSE-roost-MIT.txt` (copyright 2019 Tim Wood, David Vierra)
- The port contains 17 resource paths with Git blob SHAs identical to upstream Roost. Matches include `curtain_empty.png`, `curtain_side.png`, `hay_floor.png`, `hay_side.png`, `plain_face.png`, `slat_side.png`, `textures/gui/breeder.png`, `textures/gui/roost.png`, `textures/gui/jei.png`, and `textures/items/catcher.png`. Some assets appear twice under legacy/modern folder variants.
- CreeperHost's published mod description explicitly acknowledges merging features from timrwood's Roost.

## Obligations and limitations
Both upstream repositories publish their source under MIT notices. MIT permits use, modification and redistribution if the applicable copyright and permission notices are retained. The build packages both original notices at `META-INF/LICENSE-original-MIT.txt` and `META-INF/LICENSE-roost-MIT.txt` and fails if either is missing.

This review confirmed repository-level licenses and **exact identical file content** for the paths mentioned above. It did **not** inspect every image's full chain of authorship, third-party imported sources or all historical commits. A source repository being MIT does not automatically establish ownership of every externally contributed asset. If an asset carries independent attribution/license restrictions, those must also be respected. No separate permission request was found necessary for the identified MIT-licensed source itself.

Do not claim endorsement by CreeperHost or the Roost authors. Attribution is not a trademark license. This is a source/provenance assessment, not a legal opinion.
