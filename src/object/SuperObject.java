package object;

//import Main.UtilityTool;
//import Main.gamePanel;
//import manager.DrawManager;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
//public class SuperObject {
//
//    public BufferedImage image,image2,image3;
//    public String name;
//    public boolean collision;
//    public int worldX,worldY;
//    public Rectangle solidArea = new Rectangle(0,0,48,48);
//    public int solidAreaDefauftX = 0,solidAreaDefauftY = 0;
//    private Object gp;                                     //day nai dung oi
//
//    public void draw(Graphics2D g2) {
//
//        BufferedImage image = null;
//        DrawManager drawManager = new DrawManager(gp, this);
//
//        switch (direction) {
//            case "up":
//                if (spriteNum == 1)
//                    image = up1;
//                if (spriteNum == 2)
//                    image = up2;
//                break;
//            case "down":
//                if (spriteNum == 1)
//                    image = down1;
//                if (spriteNum == 2)
//                    image = down2;
//                break;
//            case "left":
//                if (spriteNum == 1)
//                    image = left1;
//                if (spriteNum == 2)
//                    image = left2;
//                break;
//            case "right":
//                if (spriteNum == 1)
//                    image = right1;
//                if (spriteNum == 2)
//                    image = right2;
//                break;
//        }
//
//        drawManager.draw(g2, image, worldX, worldY);
//
//    }
//
//    // THEM TRINH DOC ANH
//    public BufferedImage setup(String imagePath) {
//
//        UtilityTool uTool = new UtilityTool();
//        BufferedImage image = null;
//
//        try {
//            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//        return image;
//    }
//}