package agh.ostatni5.eomc.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class GenotypeMap extends HashMap<Genotype, LongAdder>  {

    public GenotypeMap()
    {
        super();
    }

    public void increment(Genotype genotype) {
        if(get(genotype)==null)
        {
            put(genotype,new LongAdder());
        }
        get(genotype).increment();
    }

    public void decrement(Genotype genotype) {
        get(genotype).decrement();
    }

    public Map.Entry<Genotype, LongAdder> getDominant() {
        int max = 0;
        Map.Entry<Genotype, LongAdder> maxGenotype = null;
        for (Map.Entry<Genotype, LongAdder> genotypeLongAdderEntry : entrySet()) {
            if (genotypeLongAdderEntry.getValue().intValue() >= max) {
                max = genotypeLongAdderEntry.getValue().intValue();
                maxGenotype = genotypeLongAdderEntry;
            }
        }

        return maxGenotype;
    }
    public void addAll(GenotypeMap genotypeMap)
        {
            genotypeMap.forEach((genotype, longAdder) -> {
                if(get(genotype)==null)
                {
                    put(genotype,new LongAdder());
                }
                get(genotype).add(longAdder.longValue());
            });
        }

}
