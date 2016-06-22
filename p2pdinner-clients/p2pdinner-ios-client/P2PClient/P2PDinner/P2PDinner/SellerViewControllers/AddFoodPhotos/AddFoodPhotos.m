//
//  AddFoodPhotos.m
//  P2PDinner
//
//  Created by Selvam M on 3/29/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "AddFoodPhotos.h"
#import "AFImageRequestOperation.h"
#import   "SellerHistoryHandler.h"
#import "ItemDetailsShared.h"
@interface AddFoodPhotos (){
    NSMutableArray *imageUrls;
}
@end

@implementation AddFoodPhotos
@synthesize itemDetails;
- (void)makeImageURLfromimageName:(NSArray *)imageNames
{
    NSMutableArray *mutableImageURL=[NSMutableArray arrayWithArray:imageNames];
    for (int i=0; i<[imageNames count]; i++) {
        NSString *imageURL=[imageNames objectAtIndex:i];
        imageURL=[imageURL stringByReplacingOccurrencesOfString:@"\"" withString:@""];
        [mutableImageURL replaceObjectAtIndex:i withObject:imageURL];
    
    }
    imageURLArray=nil;
    imageURLArray=[NSArray arrayWithArray:mutableImageURL];
    
}
- (void)viewDidLoad {
    self.title=@"Dinner Listing";
    
    imageURLArray=[itemDetails.imageUri componentsSeparatedByString:@","];
    imageURLMutableArray=[NSMutableArray arrayWithArray:imageURLArray];
    if ([[imageURLArray objectAtIndex:0] length]==0) {
        imageURLArray=[[NSArray alloc]init];
    }
    else
    {
        [self makeImageURLfromimageName:imageURLArray];
    }
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    //[photoSelectTableView reloadData];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma UISetupDinnerView
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    //tableView.sectionHeaderHeight=100;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 10, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:@"Plantin" size:18]];
    NSString *string=@"Add Food Photos";
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 3;
}

-
(void)imageSetForButton:(UIButton *)imgButton withCloseButtonEnable:(UIButton *)closeButton withImageURL:(NSString *)imgURL{
    [imgButton setTitle:@"Loading.." forState:UIControlStateNormal];
    [self imageRequestOperation:imgURL completionBlock:^(UIImage *img, NSError *err){
        if (!err) {
            [imgButton setTitle:@"" forState:UIControlStateNormal];
            [imgButton setBackgroundImage:img forState:UIControlStateNormal];
            [self checkEnableButton:imgButton closeBtn:closeButton];
        }
        
    }];
    
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (indexPath.row==0) {
        UITableViewCell *cell=(UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"NoteCell"];
        cell.selectionStyle =UITableViewCellSelectionStyleNone;
        
        return cell;
    }
    SelectPhotoCell *cell;
    cell= (SelectPhotoCell *)[tableView dequeueReusableCellWithIdentifier:@"AddPhotoCell"];
    
    if (indexPath.row==1) {
        UIButton *imgButton1=(UIButton *)[cell viewWithTag:11];
        UIButton *closeButton1=(UIButton *)[cell viewWithTag:1];
        if (imageURLArray.count>0) {
            [self imageSetForButton:imgButton1 withCloseButtonEnable:closeButton1 withImageURL:[imageURLArray objectAtIndex:0]];
        }
        else{
            [self checkEnableButton:imgButton1 closeBtn:closeButton1];
        }
        UIButton *imgButton2=(UIButton *)[cell viewWithTag:22];
        UIButton *closeButton2=(UIButton *)[cell viewWithTag:2];
        if (imageURLArray.count>1) {
            [self imageSetForButton:imgButton2 withCloseButtonEnable:closeButton2 withImageURL:[imageURLArray objectAtIndex:1]];
        }
        else{
            [self checkEnableButton:imgButton2 closeBtn:closeButton2];
        }
    }
    if (indexPath.row==2) {
        UIButton *imgButton1=(UIButton *)[cell viewWithTag:11];
        UIButton *closeButton1=(UIButton *)[cell viewWithTag:1];
        if (imageURLArray.count>2) {
            [self imageSetForButton:imgButton1 withCloseButtonEnable:closeButton1 withImageURL:[imageURLArray objectAtIndex:2]];
        }else
        {
            [self checkEnableButton:imgButton1 closeBtn:closeButton1];
        }
        
        UIButton *imgButton2=(UIButton *)[cell viewWithTag:22];
        UIButton *closeButton2=(UIButton *)[cell viewWithTag:2];
        if (imageURLArray.count>3) {
            [self imageSetForButton:imgButton2 withCloseButtonEnable:closeButton2 withImageURL:[imageURLArray objectAtIndex:3]];
        }else
        {
            [self checkEnableButton:imgButton2 closeBtn:closeButton2];
        }
        
    }
    
    
    cell.delegate=self;
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        return 43;
    }
    return 170;
}

- (void)checkEnableButton:(UIButton *)imageButton closeBtn:(UIButton *)closeBtn{
    if ([[imageButton backgroundImageForState:UIControlStateNormal] isEqual:[UIImage imageNamed:@"addPhoto"]]) {
        closeBtn.hidden=YES;
    }else
        closeBtn.hidden=NO;
}

- (void)imageRequestOperation:(NSString *)photoUrl completionBlock:(void(^)(UIImage *,NSError *))completion{
    
    // download the photo
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:photoUrl]];
    [request setAccessibilityLabel:@"selvam"];
    AFImageRequestOperation *operation = [AFImageRequestOperation imageRequestOperationWithRequest:request imageProcessingBlock:^UIImage *(UIImage *image) {
        
        return image;
        
    } success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
        //NSLog(@"%@",request.accessibilityLabel);
        completion(image,nil);
        
    } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error) {
        completion(nil,error);
    }];
    [operation start];
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */
#pragma mark SelectPhotoDelegate

- (void)selectPhotoAction:(id)sender{
    photoButton=(UIButton*)sender;
    [[photoButton imageView] setContentMode: UIViewContentModeCenter];
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Add Dinner photo from?"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Camera", @"Choose from Photos", nil];
    
    [actionSheet showInView:self.view];
}

#pragma mark -
#pragma mark UIActionSheetDelegate
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    UIImagePickerController * picker = [[UIImagePickerController alloc] init] ;
    int i = (int)buttonIndex;
    switch(i)
    {
        case 0:
        {
            picker.delegate = self;
            picker.sourceType = UIImagePickerControllerSourceTypeCamera;
            [self presentViewController:picker animated:YES completion:^{}];
        }
            break;
        case 1:
        {
            picker.delegate = self;
            picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
            [self presentViewController:picker animated:YES completion:^{}];
        }
        default:
            // Do Nothing.........
            break;
    }
}



#pragma mark -
#pragma - mark Selecting Image from Camera and Library
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    if (!imageUrls) {
        imageUrls=[[NSMutableArray alloc]init];
    }
    if(photoSelectTableView.frame.origin.y!=0)
    {
        [photoSelectTableView setFrame:CGRectMake(photoSelectTableView.frame.origin.x, 0, photoSelectTableView.frame.size.width, photoSelectTableView.frame.size.height)];
    }
    // Picking Image from Camera/ Library
    [picker dismissViewControllerAnimated:YES completion:^{}];
    UIImage *img=[info objectForKey:@"UIImagePickerControllerOriginalImage"];
    [photoButton setBackgroundColor:[UIColor clearColor]];
    [photoButton setTitleShadowColor:[UIColor blackColor] forState:UIControlStateNormal];
    [photoButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [photoButton setTitle:@"Uploading.." forState:UIControlStateNormal];
    [[SellerHistoryHandler sharedSellerHistoryHandler] photoUpload:img imageTag:1 buttonValue:photoButton itemDetails:itemDetails responceCallBack:^(NSError *error, NSString *imageString) {
        
        if (!error) {
            [imageUrls addObject:imageString];
            [self.itemDetails setImageUri:[imageUrls componentsJoinedByString:@","]];
        }
        else{
            UIImage *img;
                img=[UIImage imageNamed:@"addPhoto"];
                photoButton.userInteractionEnabled=NO;
            [photoButton setBackgroundImage:img forState:UIControlStateNormal];
            [self tableviewReLoad];
            UIAlertView *aleartView=[[UIAlertView alloc]initWithTitle:@"Upload Error" message:[error.userInfo description] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [aleartView show];
        }
    }];
    [photoButton setBackgroundImage:img forState:UIControlStateNormal];
    [photoSelectTableView reloadData];
}

- (void)tableviewReLoad{
    [photoSelectTableView reloadData];
}

- (void)removePhotoOf:(UITableViewCell *)cell atTag:(NSUInteger)tag{
    NSIndexPath *indexPath = [photoSelectTableView indexPathForCell:cell];
    [self removePhotoFormIndex:indexPath.row atTag:tag];
    //NSLog(@"row %ld tag %lu",(long)indexPath.row,(unsigned long)tag);
}

- (void)removePhotoFormIndex:(NSUInteger)row atTag:(NSUInteger)tag{
    if([imageURLMutableArray count]>1){
        [imageURLMutableArray removeObjectAtIndex:[self arrayObjectAtIndex:row atTag:tag]];
        itemDetails.imageUri=[imageURLMutableArray componentsJoinedByString:@","];
    }
    else if(([imageURLMutableArray count]==1) &&([[imageURLMutableArray objectAtIndex:0] length]>1)){
        itemDetails.imageUri=@"";
    }
}

- (int)arrayObjectAtIndex:(NSUInteger)row atTag:(NSUInteger)tag{
    if (row==1&&tag==1) {
        return 0;
    }
    else if (row==1&&tag==2) {
        return 1;
    }
    else if (row == 2&&tag==1) {
        return 2;
    }
    else if (row ==2&&tag==2) {
        return 3;
    }
    return 0;
}
@end
