# KoboDeluxe
Android version of Kobo Deluxe. Kobo Deluxe is a famous、open source game base on SDL(Simple DirectMedia Layer).

- Official home of Kobo Deluxe : http://olofson.net/kobodl/
- Credit to [ransj](https://github.com/ransj) for the original port of this game to Android that this repo is based on

## Download
Visit the [releases page](releases).

## Screenshots:

![screenshot 1](screenshot/device-2016-04-30-164344.png)

![screenshot 2](screenshot/device-2016-04-30-164425.png)

![screenshot 3](screenshot/device-2016-04-30-164457.png)

![screenshot 4](screenshot/device-2016-04-30-164535.png)

## Development

1. Clone this project use git
2. Import project to Android studio
3. Build the native libs by running this command in the repo root:
   
   ```
   $ANDROID_NDK_HOME/ndk-build
   ```
4. Run the command to build & package the app:
   
   ```
   ./gradlew clean build
   ```
5. Find the APK in `build/outputs/apk`

## Dependencies

This project depends on these projects below :

1. Kobo Deluxe  : http://olofson.net/kobodl/
2. SDL 2.0      : https://www.libsdl.org/
3. SDL Image    : https://www.libsdl.org/projects/SDL_image/
4. Android      : https://www.android.com/
