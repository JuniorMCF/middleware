package middleware;

import middleware.LP1.*;
import middleware.LP2.*;
import middleware.Cola;
import java.util.LinkedList;
import java.util.Queue;


public class Middleware {

	Producing productor;
	Consuming consumidor;

	public static void main(String args[]) throws InterruptedException {
		Middleware middleware = new Middleware();
		middleware.iniciar();
		/*new Thread(
			new Runnable(){
				@Override
				public void run(){
					while(true){
						nrcli = Global.nrcli;
						colas[nrcli] = new Cola();
						//System.out.println(nrcli);
					}
				}
			}
		).start();*/
	}

	void iniciar() throws InterruptedException{
		
		productor = new Producing(new Producing.OnMessageReceived() {
			@Override
			public void guardarEnCola(String message) {
				for(int i=1; i<= Global.nrcli;i++)
					if(message.contains(String.valueOf(i)))	
						Global.colas[i].addElement(message);
			}
		});

		consumidor = new Consuming(new Consuming.OnMessageReceived() {
			@Override
			public void sacarDeColaMiddleware() {
				for(int j=1 ; j<= Global.nrcli ; j++)
					if(Global.colas[j].empty())	
						consumidor.enviarDatoDeCola("Cola vacia");
					else{
						//sacamos un elemento de la cola y lo enviamos desde el servidor
						String dato = Global.colas[j].extractElement();
						consumidor.enviarDatoDeCola(dato);
					}
			}
		});

	}
/*
	void guardarElemento(String element) {
		this.queue.add(element);
	}

	String sacarCola() {
		String data = this.queue.peek();
		this.queue.poll();
		return data;
	}
*/
}
