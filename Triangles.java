/**
 * Triangle rasterization with perspective-correct texture mapping
 */
public class Triangles {
	public void perspectiveCorrectTriangle(float x1, float y1, float z1,
			float x2, float y2, float z2, float x3, float y3, float z3, int u1,
			int v1, int u2, int v2, int u3, int v3, int[] tex, int sidelen) {
		float iz1, uiz1, viz1, iz2, uiz2, viz2, iz3, uiz3, viz3;
		float dxdy1 = 0, dxdy2 = 0, dxdy3 = 0;
		float tempf;
		float det;
		float dy;
		int y1i, y2i, y3i;
		boolean side;

		x1 += 0.5f; y1 += 0.5f;
		x2 += 0.5f; y2 += 0.5f;
		x3 += 0.5f; y3 += 0.5f;
		
		iz1 = 1.0f / z1;
		iz2 = 1.0f / z2;
		iz3 = 1.0f / z3;
		uiz1 = u1 * iz1;
		viz1 = v1 * iz1;
		uiz2 = u2 * iz2;
		viz2 = v2 * iz2;
		uiz3 = u3 * iz3;
		viz3 = v3 * iz3;

		if (y1 > y2) {
			tempf = x1; x1 = x2; x2 = tempf;
			tempf = y1; y1 = y2; y2 = tempf;
			tempf = iz1; iz1 = iz2; iz2 = tempf;
			tempf = uiz1; uiz1 = uiz2; uiz2 = tempf;
			tempf = viz1; viz1 = viz2; viz2 = tempf;

		}
		if (y1 > y3) {
			tempf = x1; x1 = x3; x3 = tempf;
			tempf = y1; y1 = y3; y3 = tempf;
			tempf = iz1; iz1 = iz3; iz3 = tempf;
			tempf = uiz1; uiz1 = uiz3; uiz3 = tempf;
			tempf = viz1; viz1 = viz3; viz3 = tempf;
		}
		if (y2 > y3) {
			tempf = x2; x2 = x3; x3 = tempf;
			tempf = y2; y2 = y3; y3 = tempf;
			tempf = iz2; iz2 = iz3; iz3 = tempf;
			tempf = uiz2; uiz2 = uiz3; uiz3 = tempf;
			tempf = viz2; viz2 = viz3; viz3 = tempf;
		}
		y1i = (int) y1;
		y2i = (int) y2;
		y3i = (int) y3;

		if (y1 == y3)
			return;

		det = ((x3 - x1) * (y2 - y1) - (x2 - x1) * (y3 - y1));

		if (det == 0)
			return;
		
		det = 1 / det;
		dizdx = ((iz3 - iz1) * (y2 - y1) - (iz2 - iz1) * (y3 - y1)) * det;
		duizdx = ((uiz3 - uiz1) * (y2 - y1) - (uiz2 - uiz1) * (y3 - y1)) * det;
		dvizdx = ((viz3 - viz1) * (y2 - y1) - (viz2 - viz1) * (y3 - y1)) * det;
		dizdy = ((iz2 - iz1) * (x3 - x1) - (iz3 - iz1) * (x2 - x1)) * det;
		duizdy = ((uiz2 - uiz1) * (x3 - x1) - (uiz3 - uiz1) * (x2 - x1)) * det;
		dvizdy = ((viz2 - viz1) * (x3 - x1) - (viz3 - viz1) * (x2 - x1)) * det;

		if (y2 > y1)
			dxdy1 = (x2 - x1) / (y2 - y1);
		if (y3 > y1)
			dxdy2 = (x3 - x1) / (y3 - y1);
		if (y3 > y2)
			dxdy3 = (x3 - x2) / (y3 - y2);

		side = dxdy2 > dxdy1;

		if (y1 == y2)
			side = x1 > x2;
		if (y2 == y3)
			side = x3 > x2;

		if (!side) {
			dxdya = dxdy2;
			dizdya = dxdy2 * dizdx + dizdy;
			duizdya = dxdy2 * duizdx + duizdy;
			dvizdya = dxdy2 * dvizdx + dvizdy;

			dy = 1 - (y1 - y1i);
			xa = x1 + dy * dxdya;
			iza = iz1 + dy * dizdya;
			uiza = uiz1 + dy * duizdya;
			viza = viz1 + dy * dvizdya;

			if (y1i < y2i) {
				xb = x1 + dy * dxdy1;
				dxdyb = dxdy1;

				perspectiveCorrectSegment(y1i, y2i, tex, sidelen);
			}
			if (y2i < y3i) {
				xb = x2 + (1 - (y2 - y2i)) * dxdy3;
				dxdyb = dxdy3;

				perspectiveCorrectSegment(y2i, y3i, tex, sidelen);
			}
		} else {
			dxdyb = dxdy2;
			dy = 1 - (y1 - y1i);
			xb = x1 + dy * dxdyb;

			if (y1i < y2i) {
				dxdya = dxdy1;
				dizdya = dxdy1 * dizdx + dizdy;
				duizdya = dxdy1 * duizdx + duizdy;
				dvizdya = dxdy1 * dvizdx + dvizdy;
				xa = x1 + dy * dxdya;
				iza = iz1 + dy * dizdya;
				uiza = uiz1 + dy * duizdya;
				viza = viz1 + dy * dvizdya;

				perspectiveCorrectSegment(y1i, y2i, tex, sidelen);
			}
			if (y2i < y3i) {
				dxdya = dxdy3;
				dizdya = dxdy3 * dizdx + dizdy;
				duizdya = dxdy3 * duizdx + duizdy;
				dvizdya = dxdy3 * dvizdx + dvizdy;
				dy = 1 - (y2 - y2i);
				xa = x2 + dy * dxdya;
				iza = iz2 + dy * dizdya;
				uiza = uiz2 + dy * duizdya;
				viza = viz2 + dy * dvizdya;

				perspectiveCorrectSegment(y2i, y3i, tex, sidelen);
			}
		}
	}

	/**
	 * Utility method called by perspectiveCorrectTriangle.
	 */
	private void perspectiveCorrectSegment(int y1, int y2, int[] tex, int sidelen) {
		int scr, idx;
		int x1, x2;
		float z, u, v, dx;
		float iz, uiz, viz;

		while (y1 < y2) {
			if(y1 >= height) break;
			
			x1 = (int) xa;
			x2 = (int) xb;
			if(x1 < 0)
				x1 = 0;
			if (x2 > width - 1)
				x2 = width - 1;
			
			dx = 1 - (xa - x1);
			iz = iza + dx * dizdx;
			uiz = uiza + dx * duizdx;
			viz = viza + dx * dvizdx;
			scr = y1 * width + x1;
			
			while (x1 < x2) {
				if(scr > 0 && scr < pixels.length - 1) {
					z = 1 / iz;
					u = uiz * z;
					v = viz * z;
					
					idx = ((int) v & (sidelen - 1)) * sidelen + ((int) u & (sidelen - 1));
					pixels[scr] = tex[idx];
				}
				iz += dizdx;
				uiz += duizdx;
				viz += dvizdx;
				x1++;
				scr++;
			}
			xa += dxdya;
			xb += dxdyb;
			iza += dizdya;
			uiza += duizdya;
			viza += dvizdya;
			
			y1++;
		}
	}
}
