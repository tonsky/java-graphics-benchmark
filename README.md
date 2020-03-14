# Java Graphics benchmark

In both test I didn’t use AWT/Swing/JavaFX components/scene, but created a full-window canvas and rendered to it directly.

## AWT

```
./script/awt
```

![](screenshots/awt.png)

- 20-30 fps for 2600x1600 window
- No VSync
- Double buffering seems to be really expensive
- No way to control OpenType features (incl. no way to DISABLE e.g. `calt`)

## JavaFX 14

```
./script/bootstrap
./script/fx
```

![](screenshots/fx.png)

- Faster than AWT. Fill 2600x1600 window with 11pt text takes 2-3ms / frame
- VSync exists, capped at 60fps (?)
- No OpenType font features (no ligatures)
- No way to reuse text layout (+measures) when later rendering same text to canvas
- Drawing 32x DropShadow effects drops fps to ~30 fps
- LCD font smoothing seems to be very bad

## Test machine

```
3,2 GHz 6-Core Intel Core i7
Intel UHD Graphics 630 1536 MB
3840x2160 @ 60Hz
MacOS 10.15.3
```