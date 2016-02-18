import java.util.*;

public class Main {
  public static void main(String[] args) {
    int laizeLamination = 1300;
    int lisiereGauche = 30;
    int lisiereDroite = 30;
    int nbCouteaux = 15;
    int lisiereRecoupeGauche = 10;

    int nbLaizeDiff = 0;
    int[] listeLaizeOrder;
    int[] listeNbOrder;

    int[] listeOrderBobinots;
    int[] listeUsedBobinots;
    int nbBobinots = 0;
    int laizeProd = laizeLamination - (lisiereGauche + lisiereDroite);
    int laizeUtile = laizeProd;
    int nbBobinotsParBobine = nbCouteaux - 1;
    String logLam = "";

    System.out.println("Calcul de patron de coupe :\n");
    /*System.out.println(" Nombres de laize différentes ?");
    Scanner sc = new Scanner(System.in);
    nbLaizeDiff = sc.nextInt();
    laizeOrder = new int[nbLaizeDiff];
    nbOrder = new int[nbLaizeDiff];
    for (int i=0; i<nbLaizeDiff; i++){
      System.out.println("Entrez laize bobinot");
      laizeOrder[i] = sc.nextInt();
      System.out.println("Entrez nombre de bobinots pour : " + orderLaize[i]);
      nbOrder[i] = sc.nextInt();
    }*/

    listeLaizeOrder = new int[]{127, 178, 528, 79};
    listeNbOrder = new int[]{32, 45, 8, 25};
    nbLaizeDiff = listeLaizeOrder.length;

    /**Affiche la commande*/
    afficheCommande(listeLaizeOrder, listeNbOrder);

    /**Recupere nbre total de bobinot*/
    nbBobinots = calculNbBobinots(nbLaizeDiff, listeNbOrder);

    /**Remplit le tableau listeOrderBobinots*/
    listeOrderBobinots = peuplerListeOrderBobinots(listeLaizeOrder, listeNbOrder, nbBobinots);

    /**Remplit un tableau pour marquer comme utiliser ou non*/
    listeUsedBobinots = peuplerListeUsedBobinots(nbBobinots);

    /**Rangement sens decroissant*/
    listeOrderBobinots = rangementOrder(listeOrderBobinots);

    /**Debut de calcul de plan de découpe*/
    PlanDeDecoupe(nbBobinots, listeOrderBobinots, listeUsedBobinots, laizeLamination, laizeProd, laizeUtile, nbCouteaux, nbBobinotsParBobine, lisiereGauche, lisiereDroite, lisiereRecoupeGauche, logLam);
  }

  public static void PlanDeDecoupe(int nbBobinots,int[] listeOrderBobinots, int[] listeUsedBobinots, int laizeLamination, int laizeProd, int laizeUtile, int nbCouteaux, int nbBobinotsParBobine,int lisiereGauche, int lisiereDroite, int lisiereRecoupeGauche, String logLam){
    /**Debut de calcul de plan de découpe*/
    /**On commence avec une bobine mère*/
    int countBobines = 1;
    int countBobinots = 0;
    int countBobinotsParBobine = 0;
    int count2BobinotsParBobine = 0;
    String planDeCoupe = "";
    String planBobineMere = "";
    String pertes = "";
    Boolean modeRecoupe = false;
    int saveCoupe = 0;
    int saveRecoupe = 0;

    for(int testBobinot = 0; testBobinot < nbBobinots; testBobinot++){

      /**Si nb bobinots atteint pour le run.*/
      if ((countBobinotsParBobine == nbBobinotsParBobine)
        && (laizeUtile - lisiereRecoupeGauche - listeOrderBobinots[testBobinot] > 0)
        && (listeUsedBobinots[testBobinot] == 0)){
        //System.out.println("nb de bobinots atteint pour ce run et laize restantes : " + laizeUtile);
        logLam += "nb de bobinots atteint pour ce run et laize restantes : " + laizeUtile + "\n";
        laizeUtile -= lisiereRecoupeGauche;
        planDeCoupe += "\nrecoupe ("+laizeUtile+"mm) : ";
        modeRecoupe = true;
        saveCoupe += countBobinotsParBobine;
        countBobinotsParBobine = 0;
      }
      /**Si nb bobinots atteint pour le run en recoupe.*/
      else if ((count2BobinotsParBobine == nbBobinotsParBobine)
        && (laizeUtile - lisiereRecoupeGauche - listeOrderBobinots[testBobinot] > 0)
        && (listeUsedBobinots[testBobinot] == 0)){
        //System.out.println("en recoupe 2 ("+laizeUtile+"mm) : nb de bobinot atteint pour ce run et laize restantes : " + laizeUtile);
        logLam += "en recoupe 2 ("+laizeUtile+"mm) : nb de bobinot atteint pour ce run et laize restantes : " + laizeUtile + "\n";
        laizeUtile -= lisiereRecoupeGauche;
        planDeCoupe += "\nrecoupe 2 : ";
        modeRecoupe = true;
        saveRecoupe += count2BobinotsParBobine;
        count2BobinotsParBobine = 0;
      }
      /**Si le bobinot passe dans la laize.*/
      else if ((laizeUtile - listeOrderBobinots[testBobinot] > 0)
        && (listeUsedBobinots[testBobinot] == 0)){
        if (modeRecoupe == false){
          if (countBobinotsParBobine < nbBobinotsParBobine){
            //System.out.println("Bobinot en coupe : " + listeOrderBobinots[testBobinot] + ", laize restantes : " + laizeUtile);
            logLam += "Bobinot en coupe : " + listeOrderBobinots[testBobinot] + ", laize restantes : " + laizeUtile + "\n";
            laizeUtile -= listeOrderBobinots[testBobinot];
            countBobinotsParBobine++;
            countBobinots++;
            /**Marque bobinot comme utilisé.*/
            listeUsedBobinots[testBobinot] = 1;
            planDeCoupe += " "+ listeOrderBobinots[testBobinot];
            /**On reboucle.*/
            testBobinot = 0;
          }
        }
        else if (modeRecoupe == true){
          if(count2BobinotsParBobine < nbBobinotsParBobine){
            //System.out.println("Bobinot en recoupe : " + listeOrderBobinots[testBobinot] + ", laize restantes : " + laizeUtile);
            logLam += "Bobinot en recoupe : " + listeOrderBobinots[testBobinot] + ", laize restantes : " + laizeUtile + "\n";
            laizeUtile -= listeOrderBobinots[testBobinot];
            count2BobinotsParBobine++;
            countBobinots++;
            /**Marque bobinot comme utilisé.*/
            listeUsedBobinots[testBobinot] = 1;
            planDeCoupe += " "+ listeOrderBobinots[testBobinot];
            /**On reboucle.*/
            testBobinot = 0;
          }
        }
      }
      /**Si boucle atteinte sans avoir utilisé tous les bobinots.*/
      else if((testBobinot == nbBobinots-1)
        && (isAllBobinotsUsed(listeUsedBobinots) == false)){
        if (modeRecoupe == false){
          planBobineMere += "\nBobine " + countBobines + " (" + (countBobinotsParBobine+saveCoupe) + ")" + " : " + planDeCoupe + "\n";
          countBobinotsParBobine = 0;
        }
        else if (modeRecoupe == true){
          planBobineMere += "\nBobine " + countBobines + " (" + (count2BobinotsParBobine+saveRecoupe + saveCoupe) + ")" + " : " + planDeCoupe + "\n";
          count2BobinotsParBobine = 0;
        }
        saveCoupe = 0;
        saveRecoupe = 0;
        planDeCoupe = "";
        pertes += "de Bobine " + countBobines + " : " + laizeUtile + "\n";
        countBobines++;
        //System.out.println("Nouvelle bobine " + countBobines + " et laize restantes : " + laizeUtile);
        logLam += "Nouvelle bobine " + countBobines + " et laize restantes : " + laizeUtile + "\n";
        laizeUtile = laizeProd;
        modeRecoupe = false;
        testBobinot = 0;
      }
    }
    /**Récupère la dernière bobine mère et les pertes.*/
    planBobineMere += "\nBobine " + countBobines + " (" + countBobinotsParBobine + ")" + " : " + planDeCoupe + "\n";
    planDeCoupe = "";
    pertes += "de Bobine " + countBobines + " : " + laizeUtile + "\n";
    //System.out.println(logLam);
    System.out.println("Nombre bobine mere : " + countBobines + " de laize " + laizeLamination);
    System.out.println("Total bobinots commandés " + nbBobinots);
    System.out.println("Total bobinots produits " + countBobinots);
    System.out.println("\nBobines utilisees :\n" + planBobineMere);
    System.out.println("Chutes restantes (hors chutes gauche " + lisiereGauche + " et droit " + lisiereDroite + " :\n" + pertes);
    System.out.println("Nb de couteaux : " + nbCouteaux + " soit " + nbBobinotsParBobine + " bobinots maxi par run.");
   return;
  }

  /**Affiche la commande*/
  public static void afficheCommande(int[] listeLaizeOrder, int[] listeNbOrder){
    for (int i = 0; i < listeLaizeOrder.length; i++){
      System.out.println(listeLaizeOrder[i] + " X " + listeNbOrder[i] + "\n");
    }
    return;
  }

  /**Récupère nbre total de bobinots.*/
  public static int calculNbBobinots(int nbLaizeDiff, int[] listeNbOrder){
    int nbBobinots = 0;
    for (int i = 0; i<nbLaizeDiff; i++){
      nbBobinots += listeNbOrder[i];
    }
    return nbBobinots;
  }

  /**Remplit le tableau listeOrderBobinots.*/
  public static int[] peuplerListeOrderBobinots(int[] listeLaizeOrder, int[] listeNbOrder, int nbBobinots){
    int[] listeOrderBobinots = new int[nbBobinots];
    /**Sert à incrémenter position listeOrderBobinots.*/
    int index = 0;
    for(int i=0; i<listeNbOrder.length; i++){
      int quantite = listeNbOrder[i];
      for(int j=0; j<quantite; j++){
      listeOrderBobinots[index] = listeLaizeOrder[i];
      /**Incrémente position liste listeOrderBobinots.*/
      index++;
    }
  }
  return listeOrderBobinots;
}

/**Remplit un tableau pour marquer comme utilisé ou non.*/
public static int[] peuplerListeUsedBobinots(int nbBobinots){
  int[] listeUsedBobinots;
  listeUsedBobinots = new int[nbBobinots];
  for(int i = 0; i<nbBobinots; i++){
    listeUsedBobinots[i] = 0;
  }
  return listeUsedBobinots;
}

/**Rangement sens decroissant.*/
public static int[] rangementOrder(int[] listeOrderBobinots){
  int swap = 0;
  int nbBobinots = listeOrderBobinots.length;
  for (int n1 = 0; n1 < (nbBobinots - 1); n1++){
    for(int n2 = 0; n2 < (nbBobinots - n1 - 1); n2++){
      if (listeOrderBobinots[n2] < listeOrderBobinots[n2 + 1]){ // pour ascendant utilise >
        swap = listeOrderBobinots[n2];
        listeOrderBobinots[n2] = listeOrderBobinots[n2+1];
        listeOrderBobinots[n2+1] = swap;
        }
      }
    }
    return listeOrderBobinots;
  }

  //Calcul du plus petit bobinot.
  /*public static int minLaizeOrder(int[] listeOrderBobinots, int[] listeUsedBobinots){
    int laizeMinOrder=0;
    int nbBobinots = listeOrderBobinots.length;
    for ( int i = 0; i < nbBobinots; i++){
      for (int j = 0; j < nbBobinots; j++){
        if((listeOrderBobinots[i] < listeOrderBobinots[j]) && (listeUsedBobinots[i] == 0)){
          laizeMinOrder = listeOrderBobinots[i];
        }
      }
    }
    //System.out.println("bobinot le plus petit utilisable " + laizeMinOrder);
    return laizeMinOrder;
  }*/

  /**Vérifie que tous les bobinots ont été utilusés.*/
  public static Boolean isAllBobinotsUsed(int [] listeUsedBobinots){
    int j = 0;
    for(int i = 0; i<listeUsedBobinots.length; i++){
      j += listeUsedBobinots[i];
    }
    if(j == listeUsedBobinots.length){
      return true;
    }
    else{
      return false;
    }
  }
}
