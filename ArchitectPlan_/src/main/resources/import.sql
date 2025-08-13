insert into Project (id, name, description, url_image) values(nextval('project_seq'),'Museo di Istambul','Museo di arte moderna situato a Istanbul uno spettacolo architettonico in stile moderno','/images/renzo_piano_opera.jpeg');
insert into Project (id, name, description, url_image) values(nextval('project_seq'),'Appartamento','Appartamento in centro','/images/strato-appartamento-roma.jpg');
insert into Project (id, name, description, url_image) values(nextval('project_seq'),'Diga','Diga in cemento armato','/images/diga.jpg');
insert into Project (id, name, description, url_image) values(nextval('project_seq'),'Villa','Villa lussuosa con piscina','/images/villaConPiscina.jpg');

insert into Architect (id, name, surname, date_of_birth, description, url_image) values(nextval('architect_seq'),'Renzo','Piano','1937/09/12','Architetto italiano. Residente a Parigi e in possesso anche della cittadinanza francese, è considerato uno degli architetti più influenti, prolifici e attivi a livello internazionale', '/images/renzo-piano.jpeg');
insert into Architect (id, name, surname, date_of_birth, description, url_image) values(nextval('architect_seq'),'Zaha','Hadid','1950/10/31','Architetta e designer irachena', '/images/zaha_hadid.jpeg');
insert into Architect (id, name, surname, date_of_birth, description, url_image) values(nextval('architect_seq'),'Oscar','Niemeyer','1907/12/15','Architetto brasiliano', '/images/Oscar_Niemeyer.jpg');
insert into Architect (id, name, surname, date_of_birth, description, url_image) values(nextval('architect_seq'),'Antoni','Gaudì','1852/06/25','Architetto spagnolo esponente del modernismo catalano', '/images/Gaudi.jpg');
