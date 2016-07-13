var zones = [
		[ [ 13.037455272729183, 47.82289830824203 ], [ 13.038124229759232, 47.82367624317964 ],
				[ 13.039462143819328, 47.82367624317964 ], [ 13.040131100849377, 47.82289830824204 ],
				[ 13.039462143819328, 47.822120373304436 ], [ 13.038124229759232, 47.822120373304436 ],
				[ 13.037455272729183, 47.82289830824203 ] ],
		[ [ 13.039462143819328, 47.82367624317964 ], [ 13.04013111087455, 47.824454178117236 ],
				[ 13.041469044984993, 47.824454178117236 ], [ 13.042138012040212, 47.82367624317964 ],
				[ 13.041469044984993, 47.82289830824204 ], [ 13.04013111087455, 47.82289830824204 ],
				[ 13.039462143819328, 47.82367624317964 ] ],
		[ [ 13.041469014909476, 47.82289830824204 ], [ 13.042137971939525, 47.82367624317964 ],
				[ 13.043475885999621, 47.82367624317964 ], [ 13.04414484302967, 47.82289830824204 ],
				[ 13.043475885999621, 47.822120373304436 ], [ 13.042137971939525, 47.822120373304436 ],
				[ 13.041469014909476, 47.82289830824204 ] ],
		[ [ 13.041469014909476, 47.82134243836684 ], [ 13.042137951890451, 47.82212037330443 ],
				[ 13.043475825852399, 47.82212037330443 ], [ 13.044144762833373, 47.82134243836684 ],
				[ 13.043475825852399, 47.82056450342924 ], [ 13.042137951890451, 47.82056450342924 ],
				[ 13.041469014909476, 47.82134243836684 ] ],
		[ [ 13.039462143819328, 47.820564503429246 ], [ 13.0401310707764, 47.82134243836685 ],
				[ 13.041468924690548, 47.82134243836685 ], [ 13.04213785164762, 47.820564503429246 ],
				[ 13.041468924690548, 47.81978656849166 ], [ 13.0401310707764, 47.81978656849166 ],
				[ 13.039462143819328, 47.820564503429246 ] ],
		[ [ 13.037455272729183, 47.82134243836684 ], [ 13.038124209710158, 47.82212037330443 ],
				[ 13.039462083672106, 47.82212037330443 ], [ 13.040131020653082, 47.82134243836684 ],
				[ 13.039462083672106, 47.82056450342924 ], [ 13.038124209710158, 47.82056450342924 ],
				[ 13.037455272729183, 47.82134243836684 ] ],
		[ [ 13.039462143819328, 47.822120373304436 ], [ 13.040131090824627, 47.82289830824204 ],
				[ 13.041468984835227, 47.82289830824204 ], [ 13.042137931840525, 47.822120373304436 ],
				[ 13.041468984835227, 47.82134243836684 ], [ 13.040131090824627, 47.82134243836684 ],
				[ 13.039462143819328, 47.822120373304436 ] ] ];

var diamonds = [];

for (var i = 0; i < zones.length; ++i)
{
	var z = zones[i];
	for (var k = 0; k < z.length - 1; k += 2)
	{
		diamonds.push([ [ z[k], z[k + 1] ], [ z[k + 1], z[k + 2] ] ]);
	}
}

var gps = VV.sensor.get('GPS');
var camera = VV.sensor.get('FPV Camera 640x480');

var anzahl = 5;

var Vec = function(a)
{
	this.lng = a[0];
	this.lat = a[1];
};

Vec.prototype.sub = function(a)
{
	this.lng -= a[0];
	this.lat -= a[1];
	return this;
};

Vec.prototype.add = function(a)
{
	this.lng += a[0];
	this.lat += a[1];
	return this;
};

Vec.prototype.mul = function(s)
{
	this.lng *= s;
	this.lat *= s;
	return this;
};

function storePosition(sensorData)
{
	VV.storage.store("pos-" + k, sensorData[0]);
	VV.storage.store("img-" + k, sensorData[1]);
}

for (var k = 0; k < anzahl; ++k)
{
	var vs = diamonds[Math.floor(diamonds.length * Math.random())];
	var v1 = new Vec(vs[0][1]).sub(vs[0][0]).mul(Math.random());
	var v2 = new Vec(vs[1][1]).sub(vs[1][0]).mul(Math.random());
	var r = v1.add(vs[0][0]).add([ v2.lng, v2.lat ]);

	VV.task.execute({
		type : 'point',
		position : new VV.types.LatLngAlt(r.lat, r.lng, 50),
		tolerance : 4,
		sensors : [ gps, camera ]
	}, storePosition);
}