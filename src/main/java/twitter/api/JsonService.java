package twitter.api;

/**
 * Created by akohli on 5/30/14.
 */

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
public class JsonService {


    @GET
    @Path("/ak")
    @Produces("application/json")
    @Consumes("application/json")
    public String returnNamesList(@QueryParam("maxNumberOfItems") String show,
                                  @QueryParam("criteria") String bottomtime
                                  ) {
        System.out.println("coming here");
        System.out.println("coming here"+show+bottomtime);

        return "akhilkohli";
    }

    @GET
    @Path("/get/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String getTrackInJSON(@PathParam("name") String text) {
            System.out.println(text);
        Track track = new Track();
        track.setTitle("Enter Sandman");
        track.setSinger("Metallica");

        return text;

    }

    @GET
    @Path("/boy/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response get1TrackInJSON(@PathParam("name") String text) {
        System.out.println(text);
        Track track = new Track();
        track.setTitle("Enter Sandman");
        track.setSinger("Metallica");
        return Response.ok("oksdssdd").build();


    }

    @GET
    @Path("/newboy/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)

    public String get2TrackInJSON(@PathParam("name") String text) {
        System.out.println(text);

        return "plase";

    }


    @GET
    @Path("/tumblr/photo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String getTumblerFile(@QueryParam("show") String show,
                                 @QueryParam("bottomTime") String bottomtime,
                                 @QueryParam("upperTime") String upperTime,
                                 @QueryParam("id") String id) throws Exception
    {
        System.out.println("sssss"+show);
        System.out.println("bottom"+bottomtime);
        System.out.println("upper"+upperTime);
        System.out.println("id"+id);

        String showName=TwitterDataRetriever.getShowToTableName().get(show);
        return JumblrMain.getTumblrSqlLayer().loadPhotos(showName,bottomtime+" 00:00:00",upperTime+" 00:00:00",id);
    }

    @GET
    @Path("/tumblr/video")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String getVideoTumblerFile(@QueryParam("show") String show,
                                 @QueryParam("bottomTime") String bottomtime,
                                 @QueryParam("upperTime") String upperTime) throws Exception
    {
        String showName=TwitterDataRetriever.getShowToTableName().get(show);
        return JumblrMain.getTumblrSqlLayer().loadVideos(showName,bottomtime+" 00:00:00",upperTime+" 00:00:00");
    }


    @GET
    @Path("/tumblr/text")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String getTextTumblerFile(@QueryParam("show") String show,
                                 @QueryParam("bottomTime") String bottomtime,
                                 @QueryParam("upperTime") String upperTime) throws Exception
    {
        String showName=TwitterDataRetriever.getShowToTableName().get(show);
        return JumblrMain.getTumblrSqlLayer().loadTextPosts(showName,bottomtime+" 00:00:00",upperTime+" 00:00:00");
    }

    @GET
    @Path("/tumblr/audio")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String getAudioTumblerFile(@QueryParam("show") String show,
                                 @QueryParam("bottomTime") String bottomtime,
                                 @QueryParam("upperTime") String upperTime) throws Exception
    {
        String showName=TwitterDataRetriever.getShowToTableName().get(show);
        return JumblrMain.getTumblrSqlLayer().loadAudioPosts(showName,bottomtime+" 00:00:00",upperTime+" 00:00:00");
    }

    @GET
    @Path("/tumblr/graphs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String getGraphsTumblerFile(@QueryParam("show") String show,
                                 @QueryParam("bottomTime") String bottomtime,
                                 @QueryParam("upperTime") String upperTime) throws Exception
    {
        String showName=TwitterDataRetriever.getShowToTableName().get(show);
        return JumblrMain.getTumblrSqlLayer().loadGRaphs(showName,bottomtime+" 00:00:00",upperTime+" 00:00:00");
    }

//    @GET
//    @Path("/tumblr/numbers")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//
//    public String getNumbersTumblerFile(@QueryParam("show") String show,
//                                       @QueryParam("bottomTime") String bottomtime,
//                                       @QueryParam("upperTime") String upperTime) throws Exception
//    {
//        String showName=TwitterDataRetriever.getShowToTableName().get(show);
//        return JumblrMain.getTumblrSqlLayer().loadNumbers(showName,bottomtime+" 00:00:00",upperTime+" 00:00:00");
//    }



    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrackInJSON(Track track) {

        String result = "Track saved : " + track;
        return Response.status(201).entity(result).build();

    }
}
