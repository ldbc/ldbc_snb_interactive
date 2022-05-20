package org.ldbcouncil.snb.impls.workloads;

import com.google.common.collect.ImmutableMap;
import org.ldbcouncil.snb.driver.DbException;
import org.ldbcouncil.snb.driver.workloads.interactive.*;
import org.ldbcouncil.snb.impls.workloads.converter.Converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Superclass of query stores.
 *
 * Note: we deliberately do not use the {@link org.ldbcouncil.snb.driver.Operation#parameterMap()} method, because
 *
 * <ol>
 *   <li>some formats necessitate to handle ids differently from simple longs</li>
 *   <li>we would like to avoid any costs for type checking (e.g. using instanceof for each value)</li>
 * </ol>
 */
public abstract class QueryStore {

    protected Converter getConverter() { return new Converter(); }

    protected String getParameterPrefix() { return "$"; }

    protected String getParameterPostfix() { return ""; }

    protected Map<QueryType, String> queries = new HashMap<>();

    protected String loadQueryFromFile(String path, String filename) throws DbException {
        final String filePath = path + File.separator + filename;
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.printf("Unable to load query from file: %s", filePath);
            return null;
        }
    }

    protected String prepare(QueryType queryType, Map<String, String> parameterSubstitutions) {
        String querySpecification = queries.get(queryType);
        for (String parameter : parameterSubstitutions.keySet()) {
            querySpecification = querySpecification.replace(
                    getParameterPrefix() + parameter + getParameterPostfix(),
                    parameterSubstitutions.get(parameter)
            );
        }
        return querySpecification;
    }

    public String getParameterizedQuery(QueryType queryType) {
        return queries.get(queryType);
    }

    public enum QueryType {

        // interactive complex queries
        InteractiveComplexQuery1 ("interactive-complex-1" ),
        InteractiveComplexQuery2 ("interactive-complex-2" ),
        InteractiveComplexQuery3 ("interactive-complex-3" ),
        InteractiveComplexQuery4 ("interactive-complex-4" ),
        InteractiveComplexQuery5 ("interactive-complex-5" ),
        InteractiveComplexQuery6 ("interactive-complex-6" ),
        InteractiveComplexQuery7 ("interactive-complex-7" ),
        InteractiveComplexQuery8 ("interactive-complex-8" ),
        InteractiveComplexQuery9 ("interactive-complex-9" ),
        InteractiveComplexQuery10("interactive-complex-10"),
        InteractiveComplexQuery11("interactive-complex-11"),
        InteractiveComplexQuery12("interactive-complex-12"),
        InteractiveComplexQuery13("interactive-complex-13"),
        InteractiveComplexQuery14("interactive-complex-14"),
        InteractiveComplexQuery3DurationAsFunction ("interactive-complex-3-duration-as-function" ),
        InteractiveComplexQuery4DurationAsFunction ("interactive-complex-4-duration-as-function" ),
        InteractiveComplexQuery7WithSecond("interactive-complex-7-with-second"),

        // interactive short queries
        InteractiveShortQuery1("interactive-short-1"),
        InteractiveShortQuery2("interactive-short-2"),
        InteractiveShortQuery3("interactive-short-3"),
        InteractiveShortQuery4("interactive-short-4"),
        InteractiveShortQuery5("interactive-short-5"),
        InteractiveShortQuery6("interactive-short-6"),
        InteractiveShortQuery7("interactive-short-7"),

        // interactive updates (single queries)
        InteractiveUpdate1("interactive-update-1"),
        InteractiveUpdate2("interactive-update-2"),
        InteractiveUpdate3("interactive-update-3"),
        InteractiveUpdate4("interactive-update-4"),
        InteractiveUpdate5("interactive-update-5"),
        InteractiveUpdate6("interactive-update-6"),
        InteractiveUpdate7("interactive-update-7"),
        InteractiveUpdate8("interactive-update-8"),

        // interactive updates (additional queries for systems that perform them as multiple queries)
        InteractiveUpdate1AddPerson            ("interactive-update-1-add-person"),
        InteractiveUpdate1AddPersonCompanies   ("interactive-update-1-add-person-companies"),
        InteractiveUpdate1AddPersonEmails      ("interactive-update-1-add-person-emails"),
        InteractiveUpdate1AddPersonLanguages   ("interactive-update-1-add-person-languages"),
        InteractiveUpdate1AddPersonTags        ("interactive-update-1-add-person-tags"),
        InteractiveUpdate1AddPersonUniversities("interactive-update-1-add-person-universities"),

        InteractiveUpdate4AddForum             ("interactive-update-4-add-forum"),
        InteractiveUpdate4AddForumTags         ("interactive-update-4-add-forum-tags"),

        InteractiveUpdate6AddPost              ("interactive-update-6-add-post"),
        InteractiveUpdate6AddPostTags          ("interactive-update-6-add-post-tags"),

        InteractiveUpdate7AddComment           ("interactive-update-7-add-comment"),
        InteractiveUpdate7AddCommentTags       ("interactive-update-7-add-comment-tags"),

        // interactive updates (additional queries for system that insert content/imageFile using separate operations)
        InteractiveUpdate6AddPostContent       ("interactive-update-6-add-post-content"),
        InteractiveUpdate6AddPostImageFile     ("interactive-update-6-add-post-imagefile"),
        ;

        private String name;

        QueryType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public QueryStore(String path, String postfix) throws DbException {
        for (QueryType queryType : QueryType.values()) {
            queries.put(queryType, loadQueryFromFile(path, queryType.getName() + postfix));
        }
    }

    // Interactive complex reads

    public String getQuery1(LdbcQuery1 operation) {
        return prepare(QueryType.InteractiveComplexQuery1, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery1.PERSON_ID, getConverter().convertId(operation.getPersonIdQ1()))
                .put(LdbcQuery1.FIRST_NAME, getConverter().convertString(operation.getFirstName()))
                .build());
    }

    public String getQuery2(LdbcQuery2 operation) {
        return prepare(QueryType.InteractiveComplexQuery2, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery2.PERSON_ID, getConverter().convertId(operation.getPersonIdQ2()))
                .put(LdbcQuery2.MAX_DATE, getConverter().convertDate(operation.getMaxDate()))
                .build());
    }

    public String getQuery3(LdbcQuery3 operation) {
        return prepare(QueryType.InteractiveComplexQuery3, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery3.PERSON_ID, getConverter().convertId(operation.getPersonIdQ3()))
                .put(LdbcQuery3.COUNTRY_X_NAME, getConverter().convertString(operation.getCountryXName()))
                .put(LdbcQuery3.COUNTRY_Y_NAME, getConverter().convertString(operation.getCountryYName()))
                .put(LdbcQuery3.START_DATE, getConverter().convertDate(operation.getStartDate()))
                .put(LdbcQuery3.DURATION_DAYS, getConverter().convertInteger(operation.getDurationDays()))
                .build());
    }

    public String getQuery4(LdbcQuery4 operation) {
        return prepare(QueryType.InteractiveComplexQuery4, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery4.PERSON_ID, getConverter().convertId(operation.getPersonIdQ4()))
                .put(LdbcQuery4.START_DATE, getConverter().convertDate(operation.getStartDate()))
                .put(LdbcQuery4.DURATION_DAYS, getConverter().convertInteger(operation.getDurationDays()))
                .build());
    }

    public String getQuery5(LdbcQuery5 operation) {
        return prepare(QueryType.InteractiveComplexQuery5, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery5.PERSON_ID, getConverter().convertId(operation.getPersonIdQ5()))
                .put(LdbcQuery5.MIN_DATE, getConverter().convertDate(operation.getMinDate()))
                .build());
    }

    public String getQuery6(LdbcQuery6 operation) {
        return prepare(QueryType.InteractiveComplexQuery6, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery6.PERSON_ID, getConverter().convertId(operation.getPersonIdQ6()))
                .put(LdbcQuery6.TAG_NAME, getConverter().convertString(operation.getTagName()))
                .build());
    }

    public String getQuery7(LdbcQuery7 operation) {
        return prepare(QueryType.InteractiveComplexQuery7, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery7.PERSON_ID, getConverter().convertId(operation.getPersonIdQ7()))
                .build());
    }

    public String getQuery8(LdbcQuery8 operation) {
        return prepare(QueryType.InteractiveComplexQuery8, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery8.PERSON_ID, getConverter().convertId(operation.getPersonIdQ8()))
                .build());
    }

    public String getQuery9(LdbcQuery9 operation) {
        return prepare(QueryType.InteractiveComplexQuery9, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery9.PERSON_ID, getConverter().convertId(operation.getPersonIdQ9()))
                .put(LdbcQuery9.MAX_DATE, getConverter().convertDate(operation.getMaxDate()))
                .build());
    }

    public String getQuery10(LdbcQuery10 operation) {
        return prepare(QueryType.InteractiveComplexQuery10, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery10.PERSON_ID, getConverter().convertId(operation.getPersonIdQ10()))
                .put(LdbcQuery10.MONTH, getConverter().convertInteger(operation.getMonth()))
                .build());
    }

    public String getQuery11(LdbcQuery11 operation) {
        return prepare(QueryType.InteractiveComplexQuery11, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery11.PERSON_ID, getConverter().convertId(operation.getPersonIdQ11()))
                .put(LdbcQuery11.COUNTRY_NAME, getConverter().convertString(operation.getCountryName()))
                .put(LdbcQuery11.WORK_FROM_YEAR, getConverter().convertInteger(operation.getWorkFromYear()))
                .build());
    }

    public String getQuery12(LdbcQuery12 operation) {
        return prepare(QueryType.InteractiveComplexQuery12, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery12.PERSON_ID, getConverter().convertId(operation.getPersonIdQ12()))
                .put(LdbcQuery12.TAG_CLASS_NAME, getConverter().convertString(operation.getTagClassName()))
                .build());
    }

    public String getQuery13(LdbcQuery13 operation) {
        return prepare(QueryType.InteractiveComplexQuery13, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery13.PERSON1_ID, getConverter().convertId(operation.getPerson1IdQ13StartNode()))
                .put(LdbcQuery13.PERSON2_ID, getConverter().convertId(operation.getPerson2IdQ13EndNode()))
                .build());
    }

    public String getQuery14(LdbcQuery14 operation) {
        return prepare(QueryType.InteractiveComplexQuery14, new ImmutableMap.Builder<String, String>()
                .put(LdbcQuery14.PERSON1_ID, getConverter().convertId(operation.getPerson1IdQ14StartNode()))
                .put(LdbcQuery14.PERSON2_ID, getConverter().convertId(operation.getPerson2IdQ14EndNode()))
                .build());
    }

    // Interactive short reads

    public String getShortQuery1PersonProfile(LdbcShortQuery1PersonProfile operation) {
        return prepare(
                QueryType.InteractiveShortQuery1,
                ImmutableMap.of(LdbcShortQuery1PersonProfile.PERSON_ID, getConverter().convertId(operation.getPersonIdSQ1()))
        );
    }

    public String getShortQuery2PersonPosts(LdbcShortQuery2PersonPosts operation) {
        return prepare(
                QueryType.InteractiveShortQuery2,
                ImmutableMap.of(LdbcShortQuery2PersonPosts.PERSON_ID, getConverter().convertId(operation.getPersonIdSQ2()))
        );
    }

    public String getShortQuery3PersonFriends(LdbcShortQuery3PersonFriends operation) {
        return prepare(
                QueryType.InteractiveShortQuery3,
                ImmutableMap.of(LdbcShortQuery3PersonFriends.PERSON_ID, getConverter().convertId(operation.getPersonIdSQ3()))
        );
    }

    public String getShortQuery4MessageContent(LdbcShortQuery4MessageContent operation) {
        return prepare(
                QueryType.InteractiveShortQuery4,
                ImmutableMap.of(LdbcShortQuery4MessageContent.MESSAGE_ID, getConverter().convertId(operation.getMessageIdContent()))
        );
    }

    public String getShortQuery5MessageCreator(LdbcShortQuery5MessageCreator operation) {
        return prepare(
                QueryType.InteractiveShortQuery5,
                ImmutableMap.of(LdbcShortQuery5MessageCreator.MESSAGE_ID, getConverter().convertId(operation.getMessageIdCreator()))
        );
    }

    public String getShortQuery6MessageForum(LdbcShortQuery6MessageForum operation) {
        return prepare(
                QueryType.InteractiveShortQuery6,
                ImmutableMap.of(LdbcShortQuery6MessageForum.MESSAGE_ID, getConverter().convertId(operation.getMessageForumId()))
        );
    }

    public String getShortQuery7MessageReplies(LdbcShortQuery7MessageReplies operation) {
        return prepare(
                QueryType.InteractiveShortQuery7,
                ImmutableMap.of(LdbcShortQuery7MessageReplies.MESSAGE_ID, getConverter().convertId(operation.getMessageRepliesId()))
        );
    }

    // Interactive updates

    // Interactive updates, formulated as a single query each

    public String getUpdate1Single(LdbcUpdate1AddPerson operation) {
        return prepare(
                QueryType.InteractiveUpdate1,
                new ImmutableMap.Builder<String, String>()
                        .put(LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertIdForInsertion(operation.getPersonId()))
                        .put(LdbcUpdate1AddPerson.PERSON_FIRST_NAME, getConverter().convertString(operation.getPersonFirstName()))
                        .put(LdbcUpdate1AddPerson.PERSON_LAST_NAME, getConverter().convertString(operation.getPersonLastName()))
                        .put(LdbcUpdate1AddPerson.GENDER, getConverter().convertString(operation.getGender()))
                        .put(LdbcUpdate1AddPerson.BIRTHDAY, getConverter().convertDate(operation.getBirthday()))
                        .put(LdbcUpdate1AddPerson.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate1AddPerson.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate1AddPerson.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate1AddPerson.CITY_ID, getConverter().convertId(operation.getCityId()))
                        .put(LdbcUpdate1AddPerson.WORK_AT, getConverter().convertOrganisations(operation.getWorkAt()))
                        .put(LdbcUpdate1AddPerson.STUDY_AT, getConverter().convertOrganisations(operation.getStudyAt()))
                        .put(LdbcUpdate1AddPerson.EMAILS, getConverter().convertStringList(operation.getEmails()))
                        .put(LdbcUpdate1AddPerson.LANGUAGES, getConverter().convertStringList(operation.getLanguages()))
                        .put(LdbcUpdate1AddPerson.TAG_IDS, getConverter().convertLongList(operation.getTagIds()))
                        .build()
        );
    }

    public String getUpdate2(LdbcUpdate2AddPostLike operation) {
        return prepare(
                QueryType.InteractiveUpdate2,
                ImmutableMap.of(
                        LdbcUpdate2AddPostLike.PERSON_ID, getConverter().convertId(operation.getPersonId()),
                        LdbcUpdate2AddPostLike.POST_ID, getConverter().convertId(operation.getPostId()),
                        LdbcUpdate2AddPostLike.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate())
                )
        );
    }

    public String getUpdate3(LdbcUpdate3AddCommentLike operation) {
        return prepare(
                QueryType.InteractiveUpdate3,
                ImmutableMap.of(
                        LdbcUpdate3AddCommentLike.PERSON_ID, getConverter().convertId(operation.getPersonId()),
                        LdbcUpdate3AddCommentLike.COMMENT_ID, getConverter().convertId(operation.getCommentId()),
                        LdbcUpdate3AddCommentLike.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate())
                )
        );
    }

    public String getUpdate4Single(LdbcUpdate4AddForum operation) {
        return prepare(
                QueryType.InteractiveUpdate4,
                ImmutableMap.of(
                        LdbcUpdate4AddForum.FORUM_ID, getConverter().convertIdForInsertion(operation.getForumId()),
                        LdbcUpdate4AddForum.FORUM_TITLE, getConverter().convertString(operation.getForumTitle()),
                        LdbcUpdate4AddForum.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()),
                        LdbcUpdate4AddForum.MODERATOR_PERSON_ID, getConverter().convertId(operation.getModeratorPersonId()),
                        LdbcUpdate4AddForum.TAG_IDS, getConverter().convertLongList(operation.getTagIds())
                )
        );
    }


    public String getUpdate5(LdbcUpdate5AddForumMembership operation) {
        return prepare(
                QueryType.InteractiveUpdate5,
                ImmutableMap.of(
                        LdbcUpdate5AddForumMembership.FORUM_ID, getConverter().convertId(operation.getForumId()),
                        LdbcUpdate5AddForumMembership.PERSON_ID, getConverter().convertId(operation.getPersonId()),
                        LdbcUpdate5AddForumMembership.JOIN_DATE, getConverter().convertDateTime(operation.getJoinDate())
                )
        );
    }

    public String getUpdate6Single(LdbcUpdate6AddPost operation) {
        return prepare(
                QueryType.InteractiveUpdate6,
                new ImmutableMap.Builder<String, String>()
                        .put(LdbcUpdate6AddPost.POST_ID, getConverter().convertIdForInsertion(operation.getPostId()))
                        .put(LdbcUpdate6AddPost.IMAGE_FILE, getConverter().convertString(operation.getImageFile()))
                        .put(LdbcUpdate6AddPost.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate6AddPost.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate6AddPost.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate6AddPost.LANGUAGE, getConverter().convertString(operation.getLanguage()))
                        .put(LdbcUpdate6AddPost.CONTENT, getConverter().convertString(operation.getContent()))
                        .put(LdbcUpdate6AddPost.LENGTH, getConverter().convertInteger(operation.getLength()))
                        .put(LdbcUpdate6AddPost.AUTHOR_PERSON_ID, getConverter().convertId(operation.getAuthorPersonId()))
                        .put(LdbcUpdate6AddPost.FORUM_ID, getConverter().convertId(operation.getForumId()))
                        .put(LdbcUpdate6AddPost.COUNTRY_ID, getConverter().convertId(operation.getCountryId()))
                        .put(LdbcUpdate6AddPost.TAG_IDS, getConverter().convertLongList(operation.getTagIds()))
                        .build()
        );
    }

    public String getUpdate7Single(LdbcUpdate7AddComment operation) {
        return prepare(
                QueryType.InteractiveUpdate7,
                new ImmutableMap.Builder<String, String>()
                        .put(LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertIdForInsertion(operation.getCommentId()))
                        .put(LdbcUpdate7AddComment.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate7AddComment.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate7AddComment.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate7AddComment.CONTENT, getConverter().convertString(operation.getContent()))
                        .put(LdbcUpdate7AddComment.LENGTH, getConverter().convertInteger(operation.getLength()))
                        .put(LdbcUpdate7AddComment.AUTHOR_PERSON_ID, getConverter().convertId(operation.getAuthorPersonId()))
                        .put(LdbcUpdate7AddComment.COUNTRY_ID, getConverter().convertId(operation.getCountryId()))
                        .put(LdbcUpdate7AddComment.REPLY_TO_POST_ID, getConverter().convertId(operation.getReplyToPostId()))
                        .put(LdbcUpdate7AddComment.REPLY_TO_COMMENT_ID, getConverter().convertId(operation.getReplyToCommentId()))
                        .put(LdbcUpdate7AddComment.TAG_IDS, getConverter().convertLongList(operation.getTagIds()))
                        .build()
        );
    }

    public String getUpdate8(LdbcUpdate8AddFriendship operation) {
        return prepare(
                QueryType.InteractiveUpdate8,
                ImmutableMap.of(
                        LdbcUpdate8AddFriendship.PERSON1_ID, getConverter().convertId(operation.getPerson1Id()),
                        LdbcUpdate8AddFriendship.PERSON2_ID, getConverter().convertId(operation.getPerson2Id()),
                        LdbcUpdate8AddFriendship.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate())
                )
        );
    }

    // Interactive updates requiring multiple queries (for some systems)

    public List<String> getUpdate1Multiple(LdbcUpdate1AddPerson operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate1AddPerson,
                new ImmutableMap.Builder<String, String>()
                        .put(LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertIdForInsertion(operation.getPersonId()))
                        .put(LdbcUpdate1AddPerson.PERSON_FIRST_NAME, getConverter().convertString(operation.getPersonFirstName()))
                        .put(LdbcUpdate1AddPerson.PERSON_LAST_NAME, getConverter().convertString(operation.getPersonLastName()))
                        .put(LdbcUpdate1AddPerson.GENDER, getConverter().convertString(operation.getGender()))
                        .put(LdbcUpdate1AddPerson.BIRTHDAY, getConverter().convertDate(operation.getBirthday()))
                        .put(LdbcUpdate1AddPerson.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate1AddPerson.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate1AddPerson.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate1AddPerson.CITY_ID, getConverter().convertId(operation.getCityId()))
                        .build()
        ));

        for (LdbcUpdate1AddPerson.Organization organization : operation.getWorkAt()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonCompanies,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertIdForInsertion(operation.getPersonId()),
                            "organizationId", getConverter().convertId(organization.getOrganizationId()),
                            "worksFromYear", getConverter().convertInteger(organization.getYear())
                    )
            ));
        }
        for (String email : operation.getEmails()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonEmails,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertIdForInsertion(operation.getPersonId()),
                            "email", getConverter().convertString(email)
                    )
            ));
        }
        for (String language : operation.getLanguages()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonLanguages,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertIdForInsertion(operation.getPersonId()),
                            "language", getConverter().convertString(language)
                    )
            ));
        }

        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonTags,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertIdForInsertion(operation.getPersonId()),
                            "tagId", getConverter().convertId(tagId))
                    )
            );
        }
        for (LdbcUpdate1AddPerson.Organization organization : operation.getStudyAt()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonUniversities,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertIdForInsertion(operation.getPersonId()),
                            "organizationId", getConverter().convertId(organization.getOrganizationId()),
                            "studiesFromYear", getConverter().convertInteger(organization.getYear())
                    )
            ));
        }
        return list;
    }

    public List<String> getUpdate4Multiple(LdbcUpdate4AddForum operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate4AddForum,
                ImmutableMap.of(
                        LdbcUpdate4AddForum.FORUM_ID, getConverter().convertIdForInsertion(operation.getForumId()),
                        LdbcUpdate4AddForum.FORUM_TITLE, getConverter().convertString(operation.getForumTitle()),
                        LdbcUpdate4AddForum.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()),
                        LdbcUpdate4AddForum.MODERATOR_PERSON_ID, getConverter().convertId(operation.getModeratorPersonId())
                )
        ));

        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate4AddForumTags,
                    ImmutableMap.of(
                            LdbcUpdate4AddForum.FORUM_ID, getConverter().convertIdForInsertion(operation.getForumId()),
                            "tagId", getConverter().convertId(tagId))
                    )
            );
        }
        return list;
    }

    public List<String> getUpdate6Multiple(LdbcUpdate6AddPost operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate6AddPost,
                new ImmutableMap.Builder<String, String>()
                        .put(LdbcUpdate6AddPost.POST_ID, getConverter().convertIdForInsertion(operation.getPostId()))
                        .put(LdbcUpdate6AddPost.IMAGE_FILE, getConverter().convertString(operation.getImageFile()))
                        .put(LdbcUpdate6AddPost.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate6AddPost.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate6AddPost.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate6AddPost.LANGUAGE, getConverter().convertString(operation.getLanguage()))
                        .put(LdbcUpdate6AddPost.CONTENT, getConverter().convertString(operation.getContent()))
                        .put(LdbcUpdate6AddPost.LENGTH, getConverter().convertInteger(operation.getLength()))
                        .put(LdbcUpdate6AddPost.AUTHOR_PERSON_ID, getConverter().convertId(operation.getAuthorPersonId()))
                        .put(LdbcUpdate6AddPost.FORUM_ID, getConverter().convertId(operation.getForumId()))
                        .put(LdbcUpdate6AddPost.COUNTRY_ID, getConverter().convertId(operation.getCountryId()))
                        .build()
                )
        );
        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate6AddPostTags,
                    ImmutableMap.of(
                            LdbcUpdate6AddPost.POST_ID, getConverter().convertIdForInsertion(operation.getPostId()),
                            "tagId", getConverter().convertId(tagId))
                    )
            );
        }
        return list;
    }

    public List<String> getUpdate6MultipleSeparatedContent(LdbcUpdate6AddPost operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate6AddPost,
                new ImmutableMap.Builder<String, String>()
                        .put(LdbcUpdate6AddPost.POST_ID, getConverter().convertIdForInsertion(operation.getPostId()))
                        .put(LdbcUpdate6AddPost.IMAGE_FILE, getConverter().convertString(operation.getImageFile()))
                        .put(LdbcUpdate6AddPost.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate6AddPost.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate6AddPost.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate6AddPost.LANGUAGE, getConverter().convertString(operation.getLanguage()))
                        .put(LdbcUpdate6AddPost.CONTENT, getConverter().convertString(operation.getContent()))
                        .put(LdbcUpdate6AddPost.LENGTH, getConverter().convertInteger(operation.getLength()))
                        .put(LdbcUpdate6AddPost.AUTHOR_PERSON_ID, getConverter().convertId(operation.getAuthorPersonId()))
                        .put(LdbcUpdate6AddPost.FORUM_ID, getConverter().convertId(operation.getForumId()))
                        .put(LdbcUpdate6AddPost.COUNTRY_ID, getConverter().convertId(operation.getCountryId()))
                        .build()
                )
        );
        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate6AddPostTags,
                    ImmutableMap.of(
                            LdbcUpdate6AddPost.POST_ID, getConverter().convertIdForInsertion(operation.getPostId()),
                            "tagId", getConverter().convertId(tagId))
                    )
            );
        }
        // Check the query, and you will see why convertId needed instead of convertIdForInsertion
        list.add(prepare(QueryType.InteractiveUpdate6AddPostContent,
                ImmutableMap.of(
                        LdbcUpdate6AddPost.POST_ID, getConverter().convertId(operation.getPostId()),
                        LdbcUpdate6AddPost.CONTENT, getConverter().convertString(operation.getContent()))
                )
        );
        list.add(prepare(QueryType.InteractiveUpdate6AddPostImageFile,
                ImmutableMap.of(
                        LdbcUpdate6AddPost.POST_ID, getConverter().convertId(operation.getPostId()),
                        LdbcUpdate6AddPost.IMAGE_FILE, getConverter().convertString(operation.getImageFile()))
                )
        );

        return list;
    }

    public List<String> getUpdate7Multiple(LdbcUpdate7AddComment operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate7AddComment,
                new ImmutableMap.Builder<String, String>()
                        .put(LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertIdForInsertion(operation.getCommentId()))
                        .put(LdbcUpdate7AddComment.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate7AddComment.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate7AddComment.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate7AddComment.CONTENT, getConverter().convertString(operation.getContent()))
                        .put(LdbcUpdate7AddComment.LENGTH, getConverter().convertInteger(operation.getLength()))
                        .put(LdbcUpdate7AddComment.AUTHOR_PERSON_ID, getConverter().convertId(operation.getAuthorPersonId()))
                        .put(LdbcUpdate7AddComment.COUNTRY_ID, getConverter().convertId(operation.getCountryId()))
                        .put(LdbcUpdate7AddComment.REPLY_TO_POST_ID, getConverter().convertId(operation.getReplyToPostId()))
                        .put(LdbcUpdate7AddComment.REPLY_TO_COMMENT_ID, getConverter().convertId(operation.getReplyToCommentId()))
                        .build()
        ));
        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate7AddCommentTags,
                    ImmutableMap.of(
                            LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertIdForInsertion(operation.getCommentId()),
                            "tagId", getConverter().convertId(tagId))
                    )
            );
        }
        return list;
    }

}
