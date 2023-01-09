### Test Instructions
1. Add desired videos to `app/src/main/assets/videos`
   1. ExoPlayer doesn't recognize `.ts` files out of the box, so just rename them to `.mpeg`
2. Rebuild app
3. Videos should automatically appear in UI
4. Select desired video to start video playback
5. Errors will show up in LogCat
6. For the example videos:
   1. `bad.mpeg` should emit a `format_supported=NO_EXCEEDS_CAPABILITIES` on Pixel devices
   2. `good.mpeg` should play normally
