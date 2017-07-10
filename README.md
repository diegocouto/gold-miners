# Gold Miners (EBDI)

This is just a simple demonstration of how we can integrate [WASABI architecture](https://www.becker-asano.de/index.php/research/wasabi/61-wasabi-architecture-overview) with the traditional BDI agents provided by the [JASON platform](http://jason.sourceforge.net/wp/).

## Setup 

Apart from having JASON and WASABI running on your environment ([follow the instruction here](https://github.com/CBA2011/WASABIQtGui/blob/master/README.md)), there are some extra steps required in order to run this example:

1. When running WASABI, you have to assign globalIDs to the EmotionalAttendees which are equals to the agents names running on the JASON platoform. You can do so by editing your WASABI.ini as something like this:

```
EA miner1
globalID miner1
xmlFilename WASABI_agent_default.xml
dynFilename Becker-Asano.emo_dyn
padFilename Becker-Asano.emo_pad
simulationOn true
EA_END
```

2. On WASABI, be sure that the PAD values are enabled on the SenderMode section (it should look exactly like on the image below):

![WASABI Network Settings](https://github.com/diegocouto/gold-miners-ebdi/blob/master/doc/wasabi-network-config.png)
