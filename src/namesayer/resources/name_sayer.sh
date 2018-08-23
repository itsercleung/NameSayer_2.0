#!/bin/bash
#NAME_SAYER BASH SCRIPT
#AUTHOR: Eric Leung

function listCreations() {

    #Setting up list title
    echo -e "\n===================================================="
    echo -e "||\t\t LIST OF CREATIONS\t\t  ||"
    echo -e "====================================================\n"
    echo -e "This is the list of all existing creations:\n"

    cd creationData/ #Initially setting cd where Creations folder is
    countCreations=1 #Count of mkv files
    flagEmpty=false #Flag for if directory has mkv files or not

    #Finding if there are any available mkv files within creationData/
    if ls *.mkv &> /dev/null; then
      flagEmpty=true
    fi

    #Then listing all available creation files
    if [ $flagEmpty == true ]; then
      for creations in *.mkv; do
          echo \($countCreations\):"${creations%%.*}"
          ((countCreations++))
      done
    else
      echo "Error: There are no Creations!"
    fi

    #Option to go back to Main menu selction screen
    if [[ $flagP != true ]]; then
        while [ true ]; do
            printf "\n[Press [!] to Menu]: "
            read -r selectionVarList

            #Checking if user wants to input ! to exit to menu
            case $selectionVarList in
                ["!"]* ) cd .. && clear && menuScreen;;
                * ) echo "Error: Please enter valid command";;
            esac
        done
    fi
}

function playCreations() {
    listCreations #Recall list of Creations
    printf "\n[Press [!] to Menu]\n"
    flagPlay=false #Variable for checking if creations match user input

    while [ true ]; do
        printf "\nEnter name of creation to [PLAY] (case-sensitive): "
        read -r selectionVarPlay

        #Providing back exit to menu option
        if [[ $selectionVarPlay == "!" ]]; then
            cd ..
            flagP=false
            clear
            menuScreen
        fi

        #Checking through all creations and seeing if user input equals to name of current creations
        for creations in *.mkv; do
          flagPlay=false
            if [[ "$selectionVarPlay.mkv" = "$creations" ]]; then
                ffplay -autoexit $selectionVarPlay.mkv -hide_banner -loglevel panic
                echo "Playing $selectionVarPlay"
                flagPlay=true
            fi
        done

        #If there are no creations which equals to user input then output Error
        if [ $flagPlay == false ]; then
            echo "Error: File is either missing or mispelt!"
        fi
    done
}

function deleteCreations() {
    listCreations  #Recall list of Creations
    printf "\n[Press [!] to Menu]\n"
    flagDelete=false #Variable for checking if creations match user input

    while [ true ]; do
        printf "\nEnter name of creation to [DELETE] (case-sensitive): "
        read -r selectionVarDelete

        #Providing back exit to menu option
        if [[ $selectionVarDelete == "!" ]]; then
            cd ..
            flagP=false
            clear
            menuScreen
        fi

        #Checking through all creations and seeing if user input equals to name of current creations
        for creations in *.mkv; do
            if [[ "$selectionVarDelete.mkv" = "$creations" ]]; then
                printf "Are you sure about deleting: $selectionVarDelete? ([y]/[n]) "
                read -r confirmDelete

                #Confirming deletion, user must enter y or n
                case $confirmDelete in
                    [y]* ) echo "Deleted $selectionVarDelete" && rm ${creations%%};;
                    [n]* ) echo "Cancelled deletion";;
                    * ) echo "Error: Please enter either [y] or [n] to confirm"
                esac
                flagDelete=true
            fi
        done

        #If there are no creations which equal to user input then output Error
        if [ $flagDelete == false ]; then
            echo "Error: File is either missing or mispelt!"
        fi
    done
}

function createCreations() {
    #Setting up create title
    echo -e "\n===================================================="
    echo -e "||\t\tCREATE A CREATION\t\t  ||"
    echo -e "====================================================\n"
    echo -e "How to create a Creation?"
    echo -e "(1) Give the Creation a name"
    echo -e "(2) Record yourself saying the name (and touch-ups)"
    echo -e "(3) Done!\n"
    echo -e "[Press [!] to Menu]\n"

    #Function for recording Audio and layout
    audioRecording() {
      printf "\n(2-1): Record yourself saying the Creation name: "
      read -n 1 -s -r -p "[Press any key to start Recording]"
      echo -e "\n[RECORDING]:In-progress (5 Seconds)"

      #ffmpeg command to record voice for 5 seconds
      ffmpeg -loglevel quiet -y -f pulse -i default -t 5 $creationNameNoSpace.mp3

      echo -e "\e[1A[RECORDING]:Complete (0 Seconds)     "
    }

    cd creationData/ #Initially setting cd where Creations folder is

    while [ true ]; do
        echo -e "=--------------------------------------------------="
        printf "\n(1): Enter [Full-Name] of Creation: "
        read -r creationName

        #Removing all whitespace for ffmpeg validity
        creationNameNoSpace="$(echo -e "${creationName}" | tr -d '[:space:]')"

        #Providing back exit to menu option
        if [[ $creationName == "!" ]]; then
            cd ..
            flagP=false
            clear
            menuScreen
        fi

        #For loop that sees if userinput equals to already existing Creation, if not continue
        for creations in *.mkv; do
          if [[ ! "$creationNameNoSpace.mkv" = "$creations" ]]; then
              flagCreate=true
          else
              echo "Creation already exists! Please enter a different name"
              flagCreate=false
              break
          fi
        done

        #STEP(1) If there is no Creation that exists with creationName, then further create the video text
        if [ $flagCreate == true ]; then
            cd videoOnly/

            echo "[NAMING]:In-progress"

            #Creating video with creationName as text (runs 5 seconds)
            ffmpeg -loglevel panic -y -f lavfi -i color=c=black:s=1000x800:d=5 -vf \
                   "drawtext=fontsize=100: \
                    fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='$creationName'" \
                    $creationNameNoSpace.mkv

            echo -e "\e[1A[NAMING]:Complete   \n"
            echo -e "=--------------------------------------------------="
            cd ..
        fi

        #STEP(2)-(1) Create the audio recording for the given Creation name export to mp3
        if [ $flagCreate == true ]; then
            cd audioOnly/

            audioRecording
            echo -e "\n=--------------------------------------------------="
            cd ..
        fi

        #STEP(2)-(2) Make sure if the user is satisfied about the current recording
        if [ $flagCreate == true ]; then
          echo -e "\n(2-2): Would you like to:\n"
          echo -e "[k]eep Recording {Save as a Creation}"
          echo -e "[l]isten to the Recording"
          echo -e "[r]edo Recording"
          echo -e "[Press [!] to Menu]\n"
          cd audioOnly/

          while [ true ]; do
            printf "(2-2): Enter a selection [k/l/r/!]: "
            read -r audioSelection

            #Checking for user input for Audio Selection
            case $audioSelection in
                [kK]* ) break;;
                [lL]* ) ffplay -loglevel quiet -autoexit $creationNameNoSpace.mp3;;
                [rR]* ) audioRecording;;
                ["!"]* ) cd .. && flagP=false && clear && menuScreen ;;
                * ) echo "Error: Please enter valid command";;
            esac
          done
        fi

        #STEP(3) Make the .mkv file from step (1) and (2)
        if [ $flagCreate == true ]; then
          cd ..
          echo -e "\n=--------------------------------------------------=\n"
          echo -e "[CREATING]:In-progress"

          #ffmpeg command to create final output of both audio and video = CREATION
          ffmpeg -loglevel quiet -i audioOnly/$creationNameNoSpace.mp3 \
          -i videoOnly/$creationNameNoSpace.mkv -c copy $creationNameNoSpace.mkv

          echo -e "\e[1A[CREATING]:Completed $creationName   "
          echo -e "(3): You may now PLAY your creation \n"
          echo -e "[Press [!] to Menu]\n"
        fi
    done
}

function menuScreen() {
    #Setting up main menu screen
    echo -e "\n===================================================="
    echo -e "||\t\tWELCOME TO NAME_SAYER\t\t  ||"
    echo -e "====================================================\n"
    echo -e "Please select from one of the following options:\n"
    echo -e "[l]ist existing creations"
    echo -e "[p]lay an existing creation"
    echo -e "[d]elete an existing creation"
    echo -e "[c]reate a new creation"
    echo -e "[q]uit authoring tool \n"

    #Asks user to input selection type and handles until valid selection is given
    while [ true ]; do
        printf "Enter a selection [l/p/d/c/q]: "
        read -r selectionVarMenu
        case $selectionVarMenu in
            [lL]* ) clear && listCreations;;
            [pP]* ) flagP=true && clear && playCreations;;
            [dD]* ) flagP=true && clear && deleteCreations;;
            [cC]* ) flagP=true && clear &&createCreations;;
            [qQ]* ) echo "Exiting NAME_SAYER" && clear && exit;;
            * ) echo "Error: Please enter a valid selection";;
        esac
    done
}

function main() {
    clear
    flagP=false #Var used for idetifying list of play, delete and list

    #Creating appropriate file storage locations for creations/audio/video
    if [ ! -d ./creationData ]; then
        mkdir -p ./creationData
    fi
    if [[ ! -d creationData/videoOnly || ! -d creationData/audioOnly ]]; then
        cd creationData/
        if [[ ! -d ./videoOnly ]]; then
            mkdir -p ./videoOnly
        fi

        if [[ ! -d ./audioOnly ]]; then
            mkdir -p ./audioOnly
        fi
        cd ..
    fi

    menuScreen
}

main
