import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.ChromeCasts;
import su.litvak.chromecast.api.v2.ChromeCastsListener;

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        ChromeCast chromeCast = new ChromeCast("192.168.1.11");
        chromeCast.registerListener(x -> System.out.println(x));

        System.out.println("chromeCast.getStatus() = " + chromeCast.getMediaStatus());

//        ChromeCasts.registerListener(new ChromeCastsListener() {
//            @Override
//            public void newChromeCastDiscovered(ChromeCast chromeCast) {
//                System.out.println("Scratch.newChromeCastDiscovered");
//                System.out.println("chromeCast = " + chromeCast);
//                chromeCast.registerListener(System.out::println);
//            }
//
//            @Override
//            public void chromeCastRemoved(ChromeCast chromeCast) {
//
//            }
//        });
//
//        ChromeCasts.startDiscovery();
    }
}