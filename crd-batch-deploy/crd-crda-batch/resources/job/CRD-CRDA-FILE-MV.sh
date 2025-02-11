#!/bin/ksh


WATCH=false
COMBINE=false
UNZIP=false
LATESTONLY=false

#Get variables
for ARG in $@
do 
    echo "PROCESSING ARGUMENT:$ARG"
    
    if [[ $ARG = @(INPUT_DIR=*) ]]; then
        INPUT_DIR=${ARG##INPUT_DIR=}
    elif [[ $ARG = @(OUTPUT_DIR=*) ]]; then
        OUTPUT_DIR=${ARG##OUTPUT_DIR=}
    elif [[ $ARG = @(LIBMEM_DIR=*) ]]; then
        LIBMEM_DIR=${ARG##LIBMEM_DIR=}
    elif [[ $ARG = @(PATTERN=*) ]]; then
        PATTERN=${ARG##PATTERN=}
    elif [[ $ARG = @(JOB=*) ]]; then
        JOB=${ARG##JOB=}
    elif [[ $ARG = @(FILEVAR=*) ]]; then
        FILEVAR=${ARG##FILEVAR=}
    elif [[ $ARG = @(WATCH=*) ]]; then
        WATCH=${ARG##WATCH=}
    elif [[ $ARG = @(COMBINE=*) ]]; then
        COMBINE=${ARG##COMBINE=}
    elif [[ $ARG = @(LATESTONLY=*) ]]; then
        LATESTONLY=${ARG##LATESTONLY=}
    elif [[ $ARG = @(UNZIP=*) ]]; then
        UNZIP=${ARG##UNZIP=}
    fi
done

if [ "$INPUT_DIR" = "" ] || [ "$OUTPUT_DIR" = "" ] || [ "$LIBMEM_DIR" = "" ] || [ "$PATTERN" = "" ] || [ "$JOB" = "" ] || [ "$FILEVAR" = "" ]; then
  echo "Usage: $0 -INPUT_DIR <input-dir> -OUTPUT_DIR <output-dir> -LIBMEM_DIR <libmemsym dir> -pattern <pattern> -JOB <jobaname>"
  exit 1
fi


COMBINED_FILE=${JOB}-COMBINED.`date '+%Y%m%d_%H%M%S'`.DAT

echo //:://///////////////
echo //:: Variable List
echo //:://///////////////
echo //:: INPUT_DIR  = $INPUT_DIR
echo //:: OUTPUT_DIR = $OUTPUT_DIR
echo //:: LIBMEM_DIR = $LIBMEM_DIR
echo //:: PATTERN    = $PATTERN
echo //:: JOB        = $JOB
echo //:: FILEVAR    = $FILEVAR
echo //:: WATCH      = $WATCH
echo //:: COMBINE    = $COMBINE
echo //:: LATESTONLY = $LATESTONLY
echo //:://///////////////
echo


#echo "\c" >  ${LIBMEM_DIR}/${JOB}.txt

if [ "$WATCH" = "true" ]; then
    ctmfw $PATTERN CREATE 0 3 3 1 &
    pid=$!
    
    echo "ctmfw pid = ${pid} Started "
    
    wait $pid
    
    echo "ctmfw pid = ${pid} Ended "
fi


if [ "$COMBINE" = "true" ]; then
    echo "" >> ${LIBMEM_DIR}/${JOB}.txt
    echo %%${FILEVAR}_COMBINED=$COMBINED_FILE >> ${LIBMEM_DIR}/${JOB}.txt
    echo "" >> ${LIBMEM_DIR}/${JOB}.txt
fi

doloop="true"
if [ "$UNZIP" = "true" ]; then
  gunzip ${INPUT_DIR}/${PATTERN}.gz
  if [[ $? -ne 0 ]]; then
     echo "command failed: gunzip ${INPUT_DIR}/${PATTERN}.gz"
     exit 1
  fi
fi

for i in `ls -t ${INPUT_DIR}/${PATTERN}`
do
  echo Moving $i
  if [ "$doloop" = "true" ]; then
    echo %%${FILEVAR}=`basename $i` >> ${LIBMEM_DIR}/${JOB}.txt
    doloop="false"
  fi
  if [ "$COMBINE" = "true" ]; then
     cat $i >> $OUTPUT_DIR/$COMBINED_FILE
     if [[ $? -ne 0 ]]; then
	 echo "command failed: cat $i append $OUTPUT_DIR/$COMBINED_FILE"
	 exit 1
     fi
     rm -f $i
     if [[ $? -ne 0 ]]; then
	 echo "command failed: rm -f $i"
	 exit 1
     fi
  else
     mv $i $OUTPUT_DIR
     if [[ $? -ne 0 ]]; then
	 echo "command failed: mv $i* $OUTPUT_DIR"
	 exit 1
     fi
  fi
  if [ "$LATESTONLY" = "true" ]; then
    break
  fi
  
done
chmod o+r ${LIBMEM_DIR}/${JOB}.txt
