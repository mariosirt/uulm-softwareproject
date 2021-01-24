package com.sopra.ntts.model;

import java.util.Random;

/**
 * Class to generate Random Names.
 *
 * @author  http://www.java-gaming.org/index.php?topic=35802.0
 */
public class NameGenerator {

  private static String[] Beginning = { "James ", "John ", "Adam ", "Alvin ", "William ",
          "Helen ", "Kathleen ", "Arthur ", "Nicole ", "Julie ", "Jack ", "Beverly ", "George ", "Anthony ",
          "Lucy ", "Tracey ", "Miranda ", "Henry ", "Ruth ", "Sara ", "Bryan ", "Luis ", "Edwin ",
          "Dick ", "Beatrix " };
  private static String[] Middle = { "Selma ", "Dion ", "Molly ", "Donna ", "Albert ", "Edmund ",
          "Alexandra ", "Naomi ", "Benjamin ", "Matilda ", "Zoe ", "Lorri ", "Spencer ", "Francis ", "Wade ",
          "Henrietta ", "Anna ", "Abigail ", "Leah ", "Simon " };
  private static String[] End = { "Jones", "Harris", "Simon", "Randall", "Nelson", "Clark", "Colt",
          "Avery", "Atkinson", "Windsor", "Dayne", "Bond", "Archer" };

  private static Random rand = new Random();

  public static String generateName() {
    return Beginning[rand.nextInt(Beginning.length)] +
            Middle[rand.nextInt(Middle.length)]+
            End[rand.nextInt(End.length)];
  }

}
