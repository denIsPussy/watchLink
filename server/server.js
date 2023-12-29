const jsonServer = require('json-server');
const server = jsonServer.create();
const router = jsonServer.router('data.json'); // путь к вашему JSON файлу
const middlewares = jsonServer.defaults();
const uuid = require('uuid');

server.use(middlewares);
server.use(jsonServer.bodyParser);

server.get('/moviesByDate', (req, res) => {
    const startYear = parseInt(req.query.start);
    const endYear = parseInt(req.query.end);

    const movies = router.db.get('movies')
        .filter(movie => {
            const releaseYear = parseInt(movie.releaseYear);
            return releaseYear >= startYear && releaseYear <= endYear;
        })
        .value();

    res.jsonp(movies);
});

server.get('/moviesByGenreCount', (req, res) => {
    const movies = router.db.get('movies').value();
    const movieGenreCrossRefs = router.db.get('movieGenreCrossRefs').value();
    const genres = router.db.get('genres').value();
	console.log(movieGenreCrossRefs)

    const genreCounts = genres.map(genre => {
        const count = movieGenreCrossRefs.filter(ref => ref.genreId === genre.id).length;
        return { genre: genre, movieCount: count };
    });

    const genreCount = genreCounts.sort((a, b) => b.movieCount - a.movieCount);

    res.jsonp(genreCount);
});

server.get('/allReleaseYears', (req, res) => {
    const movies = router.db.get('movies').value();
    const releaseYears = [...new Set(movies.map(movie => movie.releaseYear))];

    releaseYears.sort((a, b) => a - b);

    res.jsonp({ releaseYears });
});

server.use(router);
server.listen(8079, () => {
    console.log('JSON Server is running');
});
