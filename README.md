# Java Graphics benchmark

This is a benchmark of 2D graphics in AWT/JavaFX. No UI components are used, just a single full-window Graphics2D/GraphicsContext/Canvas.

## Running

AWT:

```
python script/awt.py
```

JavaFX 15:

```
python script/fx.py
```

VSync demo requires command-line flag:

```
python script/fx.py --vsync
```

Skija 0.6.32:

```
python script/skija.py
```

## Usage

Use ←→ to switch between scenes.

Use ↑↓ to switch between scene parameters (if supported).

FPS counter is printed to console and to window title.

# Results

Frames per second: more is better.

Platform | Scaling | Engine | Circles | Clip | Gradients | Shadows | Words |
---------|---------|--------|---------|------|-----------|---------|-------|
macOS    | 1x      | Skija  | 110     | 180  | 120       | 750     | 32    |
macOS    | 1x      | JavaFX | 520     | < 1  | 370       | 14      | 320   |
macOS    | 2x      | Skija  | 105     | 165  | 115       | 345     | 35    |
macOS    | 2x      | JavaFX | 275     | < 1  | 305       | 13      | 335   |

Discovered JavaFX issues:

- Path clips and shadows are slow beyond usable
- Doesn’t change VSync interval when switching between 120/60 Hz monitors
- GraphicsContext does not support Arabic text or emoji
- Rendered text too bold on macOS
- Text with LCD smoothing renders very badly on macOS
- Path clip ignores FillRule

Missing JavaFX features:

- ColorSpace for render target
- OpenType features for text
- Variable fonts

Test machines:

- macOS: MacBook Pro (15-inch, 2019) 2.3 GHz 8-Core Intel Core i9 Radeon Pro 560X 4 GB

Versions:

- JavaFX: 15
- Skija: 0.6.37
- LWJGL: 3.2.3
