package com.fractal.custom.pig.udf;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

public class Power extends EvalFunc<Tuple>{

	@Override
	public Tuple exec(Tuple input) throws IOException {
		int base = (Integer) input.get(0);
		int exp = (Integer) input.get(1);
		double result = 1.0;
		Tuple out = TupleFactory.getInstance().newTuple(3);
		
		for(int i=1;i<=exp;i++){
			result = result*base;
		}
		out.set(0, input.get(0));
		out.set(1, input.get(1));
		out.set(2, result);
		
		
		return out;
	}
	

}
